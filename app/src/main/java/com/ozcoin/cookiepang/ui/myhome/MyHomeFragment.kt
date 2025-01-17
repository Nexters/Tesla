package com.ozcoin.cookiepang.ui.myhome

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.ozcoin.cookiepang.R
import com.ozcoin.cookiepang.adapter.MyHomeViewPagerAdapter
import com.ozcoin.cookiepang.base.BaseFragment
import com.ozcoin.cookiepang.databinding.FragmentMyHomeBinding
import com.ozcoin.cookiepang.databinding.ItemMyHomeTabBinding
import com.ozcoin.cookiepang.ui.MainActivityViewModel
import com.ozcoin.cookiepang.utils.observer.EventObserver
import com.ozcoin.cookiepang.utils.observer.UiStateObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class MyHomeFragment : BaseFragment<FragmentMyHomeBinding>() {

    companion object {
        private const val TAB_TYPE_COLLECTED = 0
        private const val TAB_TYPE_CREATED = 1
        private const val TAB_TYPE_QUESTION = 2
    }

    private val mainActivityViewModel by activityViewModels<MainActivityViewModel>()
    private val myHomeFragmentViewModel by viewModels<MyHomeFragmentViewModel>()
    private lateinit var myHomeViewPagerAdapter: MyHomeViewPagerAdapter

    override fun getLayoutRes(): Int {
        return R.layout.fragment_my_home
    }

    override fun initView() {
        with(binding) {
            isShareAvailable = true
            viewModel = myHomeFragmentViewModel
        }
        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        with(binding.vpPager) {
            myHomeViewPagerAdapter = MyHomeViewPagerAdapter(this@MyHomeFragment)
            isUserInputEnabled = false
            adapter = myHomeViewPagerAdapter
        }
    }

    override fun initListener() {
    }

    override fun init() {
        myHomeFragmentViewModel.loadUserInfo(getUserId())
        checkIsAskRequested()
    }

    /**
     * delay 적용하지 않고 tab 이동 시 다른 페이지가 렌더링되는 이슈 존재
     * (fragment 에서 list notify 를 main 스레드해서 그런가 ??)
     */
    private fun checkIsAskRequested() {
        viewLifecycleScope.launch {
            withContext(Dispatchers.Default) {
                delay(100L)
            }
            if (isAskRequested()) moveToAskTab()
        }
    }

    private fun getUserId(): String {
        val args by navArgs<MyHomeFragmentArgs>()
        return args.userId.also {
            Timber.d("request UserId : $it")
        }
    }

    private fun isAskRequested(): Boolean {
        val args by navArgs<MyHomeFragmentArgs>()
        return args.isAskRequested.also {
            Timber.d("isAskRequested : $it")
        }
    }

    private fun moveToAskTab() {
        binding.vpPager.setCurrentItem(2, true)
    }

    override fun initObserve() {
        observeEvent(myHomeFragmentViewModel)
        observeCollectedCookieList()
        observeCreatedCookieList()
        observeQuestionList()
        myHomeFragmentViewModel.uiStateObserver =
            UiStateObserver(mainActivityViewModel::updateUiState)
        myHomeFragmentViewModel.eventObserver = EventObserver(mainActivityViewModel::updateEvent)
    }

    private fun observeCollectedCookieList() {
        viewLifecycleScope.launch {
            myHomeFragmentViewModel.collectedCookieList.collect {
                updateTabCount(TAB_TYPE_COLLECTED, it.size.toString())
            }
        }
    }

    private fun observeCreatedCookieList() {
        viewLifecycleScope.launch {
            myHomeFragmentViewModel.createdCookieList.collect {
                updateTabCount(TAB_TYPE_CREATED, it.size.toString())
            }
        }
    }

    private fun observeQuestionList() {
        viewLifecycleScope.launch {
            myHomeFragmentViewModel.questionList.collect {
                updateTabCount(TAB_TYPE_QUESTION, it.size.toString())
            }
        }
    }

    private fun updateTabCount(tabType: Int, count: String) {
        val binding = when (tabType) {
            TAB_TYPE_COLLECTED -> {
                binding.tlTabLayout.getTabAt(0)?.customView?.tag as? ItemMyHomeTabBinding
            }
            TAB_TYPE_CREATED -> {
                binding.tlTabLayout.getTabAt(1)?.customView?.tag as? ItemMyHomeTabBinding
            }
            TAB_TYPE_QUESTION -> {
                binding.tlTabLayout.getTabAt(2)?.customView?.tag as? ItemMyHomeTabBinding
            }
            else -> null
        }
        binding?.count = count
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tlTabLayout, binding.vpPager) { tab, position ->
            val binding = ItemMyHomeTabBinding.inflate(layoutInflater, null, false)
            when (position) {
                0 -> {
                    binding.count = ""
                    binding.tabName = "소유한 쿠키"
                }
                1 -> {
                    binding.count = ""
                    binding.tabName = "제작한 쿠키"
                }
                2 -> {
                    binding.count = ""
                    binding.tabName = "받은 요청"
                }
                else -> {}
            }

            tab.customView = binding.root
            tab.customView?.tag = binding
        }.attach()
    }
}
