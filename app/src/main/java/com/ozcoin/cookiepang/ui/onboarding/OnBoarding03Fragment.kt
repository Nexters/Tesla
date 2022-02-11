package com.ozcoin.cookiepang.ui.onboarding

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ozcoin.cookiepang.R
import com.ozcoin.cookiepang.base.BaseFragment
import com.ozcoin.cookiepang.databinding.FragmentOnBoarding03Binding
import com.ozcoin.cookiepang.ui.splash.SplashActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoarding03Fragment : BaseFragment<FragmentOnBoarding03Binding>() {

    private val splashActivityViewModel by activityViewModels<SplashActivityViewModel>()
    private val onBoarding03FragmentViewModel by viewModels<OnBoarding03FragmentViewModel>()

    override fun getLayoutRes(): Int {
        return R.layout.fragment_on_boarding_03
    }

    override fun initView() {
    }

    override fun initListener() {
        binding.includeTitleLayout.tvSkipBtn.setOnClickListener {
            onBoarding03FragmentViewModel.clickSkip()
        }
    }

    override fun initObserve() {
        with(onBoarding03FragmentViewModel) {
            lifecycleScope.launch {
                eventFlow.collect { handleEvent(it) }
            }
        }
    }

    override fun init() {
        onBoarding03FragmentViewModel.finishActivity = splashActivityViewModel::finishActivity
    }
}
