package com.erros.minimax.fenix.view.main

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erros.minimax.fenix.R
import com.erros.minimax.fenix.data.Person
import com.erros.minimax.fenix.di.MainActivityModule
import com.erros.minimax.fenix.di.Scopes
import com.erros.minimax.fenix.view.base.BaseActivity
import com.erros.minimax.fenix.view.base.Init
import com.erros.minimax.fenix.view.base.Msg
import com.erros.minimax.fenix.view.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_person.view.*
import toothpick.Toothpick
import javax.inject.Inject

class MainActivity : BaseActivity<MainView, MainPresenter>(), MainView {

    @Inject
    lateinit var presenter: MainPresenter

    override val layoutId: Int
        get() = R.layout.activity_main

    override val firstMsg: Msg
        get() = Init
    private val adapter = PersonAdapter()

    init {
        Toothpick.openScopes(Scopes.APP, Scopes.MAIN_SCREEN).apply {
            installModules(MainActivityModule())
            Toothpick.inject(this@MainActivity, this)
        }
    }

    override fun getBasePresenter(): MainPresenter = presenter

    override fun onViewCreated() {
        swipeRefresh.setOnRefreshListener { presenter.onRefresh() }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun changeProgressVisibility(visible: Boolean) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun changeRefreshVisibility(visible: Boolean) {
        swipeRefresh.isRefreshing = visible
    }

    override fun showList(persons: List<Person>) {
        adapter.setCollection(persons)
    }

    override fun showPostsForUser(personId: Int) {
        DetailActivity.start(this, personId)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
        Toothpick.closeScope(Scopes.MAIN_SCREEN)
    }

    inner class PersonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val persons = mutableListOf<Person>()

        fun setCollection(persons: List<Person>) {
            this.persons.clear()
            this.persons.addAll(persons)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as PersonViewHolder).bind(persons[position])
        }

        override fun getItemCount(): Int = persons.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return PersonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false))
        }

        inner class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun bind(person: Person) {
                itemView.personTextView.text = person.name
                itemView.setOnClickListener {
                    presenter.onPersonClick(person)
                }
            }

        }
    }
}