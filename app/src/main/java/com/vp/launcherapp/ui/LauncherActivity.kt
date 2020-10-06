package com.vp.launcherapp.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.skydoves.balloon.*
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.vp.launchersdk.model.AppInfo
import com.vp.launcherapp.LauncherApplication
import com.vp.launcherapp.R
import com.vp.launcherapp.di.component.DaggerActivityComponent
import com.vp.launcherapp.di.module.ActivityModule
import kotlinx.android.synthetic.main.activity_launcher.*
import kotlinx.android.synthetic.main.layout_non_sliding.*
import kotlinx.android.synthetic.main.layout_sliding.*
import javax.inject.Inject

class LauncherActivity : AppCompatActivity(),
    LauncherAdapter.OnClickListeners {


    lateinit var viewModel: LauncherViewModel

    @Inject
    lateinit var layoutManager: LinearLayoutManager


    lateinit var launcherAdapter: LauncherAdapter

    private lateinit var popup: Balloon

    companion object {
        const val TAG = "LauncherActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)



        setUpDependencies()
        setUpRecyclerView()
        setUpViewModel()
        setUpListeners()

    }

    private fun setUpRecyclerView() {
        launcherAdapter =
            LauncherAdapter(this, arrayListOf())
        rvHideApps.layoutManager = layoutManager
        rvHideApps.adapter = launcherAdapter

    }

    private fun setUpListeners() {
        clParent.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                ivDragIcon.alpha = 1 - slideOffset
                clSearchBar.alpha = slideOffset
                clWallpaperIcon.alpha = 1 - slideOffset
            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {
                when (newState) {
                    SlidingUpPanelLayout.PanelState.EXPANDED -> {
                    }
                    SlidingUpPanelLayout.PanelState.DRAGGING -> {
                    }
                    SlidingUpPanelLayout.PanelState.COLLAPSED -> {

                        etSearch.setText("")

                    }
                }
            }
        })

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.filterApps(char.toString())
            }
        })
    }

    private fun setUpDependencies() {
        DaggerActivityComponent
            .builder()
            .applciationComponent((applicationContext as LauncherApplication).appComponent)
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)


        viewModel = (applicationContext as LauncherApplication).viewModel
    }

    private fun setUpViewModel() {
        viewModel.appData.observe(this, object : Observer<List<AppInfo>> {
            override fun onChanged(t: List<AppInfo>?) {
                Log.v(TAG, t?.size.toString())

                launcherAdapter.updateData(t)
            }
        })

        viewModel.filterData.observe(this, object : Observer<List<AppInfo>> {
            override fun onChanged(t: List<AppInfo>?) {
                Log.v(TAG, t?.size.toString())

                launcherAdapter.updateData(t)
            }
        })
    }

    override fun onBackPressed() {
        if (clParent.panelState === SlidingUpPanelLayout.PanelState.EXPANDED) {
            clParent.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        } else
            super.onBackPressed()
    }

    override fun onItemClick(position: Int, parent: View, appInfo: AppInfo) {
        packageManager.getLaunchIntentForPackage(appInfo.packageName).let { startActivity(it) }

    }

    override fun onLongItemClick(position: Int, parent: View, appInfo: AppInfo) {
        showPopup(position, parent, appInfo)
    }

    private fun showPopup(position: Int, view: View, appInfo: AppInfo) {
        popup = createBalloon(this) {
            setArrowVisible(true)
            setArrowSize(10)
            setArrowPosition(0.5f)
            setArrowConstraints(ArrowConstraints.ALIGN_ANCHOR)
            setCircularDuration(200)
            setArrowColor(ContextCompat.getColor(this@LauncherActivity, R.color.light_grey))
            setArrowOrientation(ArrowOrientation.BOTTOM)
            setBackgroundColor(ContextCompat.getColor(this@LauncherActivity,R.color.transparent))
            setBalloonAnimation(BalloonAnimation.OVERSHOOT)
            setLayout(R.layout.popup_layout)
            setDismissWhenTouchOutside(true)
            setOnBalloonDismissListener {
                clParent.isTouchEnabled = true
                rvHideApps.isLayoutFrozen = false
            }
            setLifecycleOwner(this@LauncherActivity)
        }
        popup.getContentView().findViewById<LinearLayout>(R.id.ivUninstall).setOnClickListener {
            popup.dismiss()
            startUninstall(appInfo)
        }

        popup.getContentView().findViewById<LinearLayout>(R.id.ivAppInfo).setOnClickListener {
            popup.dismiss()
            displayAppInfo(appInfo)
        }
        popup.showAlignTop(view, 0, 40)
    }

    private fun displayAppInfo(appInfo: AppInfo) {
        try {
            startActivity(
                Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                    Uri.parse("package:${appInfo.packageName}")
                )
            )
        }catch (e: ActivityNotFoundException){
            startActivity(
                Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS).setData(
                    Uri.parse("package:${appInfo.packageName}")
                )
            )
        }


    }

    private fun startUninstall(appInfo: AppInfo) {
        try {

            startActivity(
                Intent(Intent.ACTION_DELETE).setData(
                    Uri.parse("package:${appInfo.packageName}")
                )
            )

          //  viewModel.isAppUninstalled(appInfo)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
       // (applicationContext as LauncherApplication).unregisterAppBroadCast()
    }
}