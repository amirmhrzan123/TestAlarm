package com.example.sialarm.ui.tutorial

import android.content.Intent
import android.os.Bundle
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseActivity
import com.example.sialarm.databinding.ActivityTutorialBinding
import com.example.sialarm.ui.tutorial.firstTutorial.FirstStepFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class TutorialActivity:BaseActivity<TutorialViewModel,ActivityTutorialBinding>(),FragmentListener{

    private val tutorialViewModel: TutorialViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.activity_tutorial

    override fun getViewModel(): TutorialViewModel = tutorialViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

    }

    override fun openFirstTutorial(){
        supportFragmentManager
            .beginTransaction()
            .disallowAddToBackStack()
            .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
            .replace(R.id.container, FirstStepFragment.newInstance())
            .commit()
    }

    override fun openSecondTutorial(){
        supportFragmentManager
            .beginTransaction()
            .disallowAddToBackStack()
            .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
            .replace(R.id.container,SecondStepFragment.newInstance())
            .commit()

    }

    override fun openThirdTutorial(){
        supportFragmentManager
            .beginTransaction()
            .disallowAddToBackStack()
            .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
            .replace(R.id.container,ThirdStepFragment.newInstance())
            .commit()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}