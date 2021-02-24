package com.ofcat.baseframe

import android.os.Bundle
import com.ofcat.baseframe.base.BaseActivity

class MainActivity : BaseActivity(), MainContract.View {

    override fun initial(savedInstanceState: Bundle?) {
    }

    override fun onResource(): Int {
        return R.layout.activity_main
    }

    override fun showBaseLoadingDialog() {
    }

    override fun hideBaseLoadingDialog() {
    }

    override fun getUid(): String = ""

    override fun getToken(): String = ""

    override fun showSearchPage() {
    }
}