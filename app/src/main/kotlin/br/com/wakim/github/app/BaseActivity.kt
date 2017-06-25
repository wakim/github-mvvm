package br.com.wakim.github.app

import android.os.Bundle
import br.com.wakim.github.App
import br.com.wakim.github.injection.ConfigPersistentComponent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.util.concurrent.atomic.AtomicLong

@Suppress("LeakingThis")
open class BaseActivity : RxAppCompatActivity() {

    companion object {
        @JvmStatic private val KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID"
        @JvmStatic private val NEXT_ID = AtomicLong(0)
        @JvmStatic private val componentsMap = HashMap<Long, ConfigPersistentComponent>()
    }

    private var wasDestroyed = false
    private var instanceSaved = false
    private var activityId: Long = 0

    lateinit var component: ConfigPersistentComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        wasDestroyed = false

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        activityId = savedInstanceState?.getLong(KEY_ACTIVITY_ID) ?: NEXT_ID.getAndIncrement()

        component = componentsMap.getOrPut(activityId, {
            val component = (applicationContext as App).appComponent
            component.configPersistentComponent()
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_ACTIVITY_ID, activityId)

        instanceSaved = true
    }

    override fun onDestroy() {
        if (!instanceSaved) {
            componentsMap.remove(activityId)

            disposeConfigPersistentResources()
        }

        wasDestroyed = true
        super.onDestroy()
    }

    open fun disposeConfigPersistentResources() {}
}