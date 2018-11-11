package com.erros.minimax.fenix.view.main

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erros.minimax.fenix.R
import com.erros.minimax.fenix.data.Person
import com.erros.minimax.fenix.di.Scopes
import com.erros.minimax.fenix.view.base.BaseActivity
import com.erros.minimax.fenix.view.base.Init
import com.erros.minimax.fenix.view.base.Msg
import com.erros.minimax.fenix.view.base.Refresh
import com.erros.minimax.fenix.view.detail.DetailActivity
import com.erros.minimax.fenix.view.utils.showErrorMessage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_person.view.*
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.android.scope.ext.android.bindScope

class MainActivity : BaseActivity<MainView, MainPresenter>(), MainView {

    init {
        bindScope(getKoin().getOrCreateScope(Scopes.MAIN_SCOPE))
    }

    override val presenter: MainPresenter by inject()

    override val layoutId: Int
        get() = R.layout.activity_main

    override val initialMsg: Msg
        get() = Init

    private val adapter = PersonAdapter()

    override fun onViewCreated() {
        title = "Users"
        swipeRefresh.setOnRefreshListener { presenter.accept(Refresh) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun setProgressVisibility(visible: Boolean) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun setRefreshVisibility(visible: Boolean) {
        swipeRefresh.isRefreshing = visible
    }

    override fun showError(msg: String) {
        showErrorMessage(msg)
    }

    override fun showPersons(persons: List<Person>) {
        adapter.setCollection(persons)
    }

    override fun markPersonAsChosen(person: Person) {
        adapter.markPersonAsChosen(person)
    }

    override fun openPostsForUser(personId: Int) {
        DetailActivity.start(this, personId)
    }

    inner class PersonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val persons = mutableListOf<Person>()
        private var chosenPerson: Person? = null

        fun setCollection(persons: List<Person>) {
            this.persons.clear()
            this.persons.addAll(persons)
            notifyDataSetChanged()
        }

        fun markPersonAsChosen(person: Person) {
            this.chosenPerson = person
            persons.indexOf(person).takeIf { it >= 0 }?.let { notifyItemChanged(it) }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as PersonViewHolder).bind(persons[position])
        }

        override fun getItemCount(): Int = persons.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return PersonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false))
        }

        inner class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            init {
                itemView.setOnClickListener {
                    person?.let { presenter.accept(MainPresenter.OnPersonClickMsg(it)) }
                }
            }

            private var person: Person? = null

            fun bind(person: Person) {
                this.person = person
                itemView.personNameTextView.text = person.name
                itemView.personEmailTextView.text = person.email
                itemView.personPhoneTextView.text = person.phone
                itemView.setBackgroundColor(if (person == chosenPerson) Color.LTGRAY else Color.TRANSPARENT)
            }

        }
    }
}