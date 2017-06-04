package br.com.wakim.github.app

open class BaseActivity : AppCompatActivity(), LifecycleRegistryOwner {
    private val registry = LifecycleRegistry(this)
    override fun getLifecycle() = registry
}