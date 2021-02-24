package com.ofcat.baseframe.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.ofcat.baseframe.R
import com.ofcat.baseframe.tool.RxBindingTools
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class BaseActivity : AppCompatActivity() {

    var mDisposable: Disposable? = null
    var mVersionDisposable: Disposable? = null
    var mToast: Toast? = null

    private var _mComposite: CompositeDisposable? = null
    val mComposite: CompositeDisposable
        get() {
            return _mComposite ?: run {
                val instance = CompositeDisposable()
                _mComposite = instance

                return@run instance
            }
        }

    private var _mPreferences: RxSharedPreferences? = null
    val mPreferences: RxSharedPreferences
        get() {
            return _mPreferences ?: run {
                @Suppress("DEPRECATION")
                val instance = RxSharedPreferences.create(
                    PreferenceManager.getDefaultSharedPreferences(this)
                )
                _mPreferences = instance

                return@run instance
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Add window flags
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Set activity content view
        setContentView(onResource())

        // Apply system bar styles
        applySystemBarStatus()

        // Initialize views
        initial(savedInstanceState)
    }

    abstract fun initial(savedInstanceState: Bundle?)

    abstract fun onResource(): Int

    fun View.clicks(): Observable<Any> = RxBindingTools.onClicks(this)

    fun TextView.changes(): Observable<String> = RxBindingTools.onChanges(this)

    fun CompoundButton.checks(): Observable<Boolean> = RxBindingTools.onChecks(this)

    fun Disposable.composite() = mComposite.add(this)

    inline fun <reified T : Activity> Activity.start(initializer: Intent.() -> Unit) {
        startActivity(Intent(this@BaseActivity, T::class.java).apply(initializer))
        overridePendingTransition(R.anim.anim_start_enter, R.anim.anim_start_out)
    }

    inline fun <reified T : Activity> Activity.startForResult(requestCode: Int, initializer: Intent.() -> Unit) {
        startActivityForResult(Intent(this@BaseActivity, T::class.java).apply(initializer), requestCode)
        overridePendingTransition(R.anim.anim_start_enter, R.anim.anim_start_out)
    }

    inline fun <reified T : Fragment> T.start(argsBuilder: Bundle.() -> Unit): T {
        return apply { arguments = Bundle().apply(argsBuilder) }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            applySystemBarStatus()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Override activity transition
        overridePendingTransition(R.anim.anim_end_enter, R.anim.anim_end_out)
    }

    override fun onResume() {
        super.onResume()
        // Apply system bar
        applySystemBarStatus()

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Dispose composite disposable
        mComposite.dispose()

        // Dispose disposable
        mDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }


    abstract fun showBaseLoadingDialog()

    abstract fun hideBaseLoadingDialog()

    @ColorInt
    open fun getStatusBarColor(): Int {
        return Color.parseColor("#083dd9")
    }

    fun applyStatusBarStatus(@NonNull view: View) {
        val params = view.layoutParams as LinearLayout.LayoutParams
        params.height = Observable.just("status_bar_height")
            .subscribeOn(Schedulers.newThread())
            .map { s -> resources.getIdentifier(s, "dimen", "android") }
            .map { integer -> resources.getDimensionPixelSize(integer) }
            .onErrorReturn { 0 }
            .blockingSingle(0)
        view.layoutParams = params
    }

    fun applySystemBarStatus() {
        window?.let {
            it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                it.statusBarColor = getStatusBarColor()
                it.navigationBarColor = Color.BLACK
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun applyHideKeyboard(view: View?) {
        if (view is ViewGroup) {
            for (index in 0 until view.childCount) {
                applyHideKeyboard(view.getChildAt(index))
            }
        }

        view?.let {
            if (view !is EditText) {
                view.setOnTouchListener { _, _ ->
                    val context = it.context
                    val service = context?.getSystemService(Context.INPUT_METHOD_SERVICE)
                    val manager = service as? InputMethodManager
                    manager?.hideSoftInputFromWindow(it.windowToken, 0)

                    return@setOnTouchListener false
                }
            }
        }
    }

}