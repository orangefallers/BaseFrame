package com.ofcat.baseframe

interface MainContract {

    interface View {

        /**
         * Get user id
         * @return User id
         */
        fun getUid(): String

        /**
         * Get login token
         * @return Login token
         */
        fun getToken(): String

        /**
         * Show page inside loading widget
         */
        fun showBaseLoadingDialog()

        /**
         * Hide page inside loading widget
         */
        fun hideBaseLoadingDialog()

        /**
         * Show search page
         */
        fun showSearchPage()

    }

    interface Presenter {

        /**
         * Initial presenter
         */
        fun initial()

        /**
         * On search bar button click
         */
        fun onSearchBarClick()

        /**
         * On login button click
         */
        fun onLoginClick()

        /**
         * On page destroy
         */
        fun onDestroy()
    }
}