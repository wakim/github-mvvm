package br.com.wakim.github.ui.repository_list

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.wakim.github.R
import br.com.wakim.github.databinding.ActivityRepositoryListBinding

class RepositoryListActivity : AppCompatActivity() {

    lateinit var binding: ActivityRepositoryListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityRepositoryListBinding>(this, R.layout.activity_repository_list)

        setupToolbar()
    }

    fun setupToolbar() {
        setSupportActionBar(binding.includeAppBar.toolbar)
    }
}
