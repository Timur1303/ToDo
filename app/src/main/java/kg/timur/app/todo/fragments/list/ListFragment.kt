package kg.timur.app.todo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

import kg.timur.app.todo.R
import kg.timur.app.todo.data.models.ToDoData
import kg.timur.app.todo.data.viewmodel.ToDoViewModel
import kg.timur.app.todo.fragments.ShareViewModel
import kg.timur.app.todo.fragments.list.adapter.ListAdapter
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.text.ParsePosition


class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val shareViewModel: ShareViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.itemAnimator = SlideInUpAnimator().apply { addDuration = 300 }
        swipeToDelete(recyclerView)

        toDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            shareViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })
        /*shareViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabase(it)
        })*/

        view.btn_add.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment) }

        view.listLayout.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_updateFragment) }


        setHasOptionsMenu(true)


        return view
    }


    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeDeleteCallback = object : SwipeDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                toDoViewModel.delete(itemToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                restoreDeletedData(viewHolder.itemView, itemToDelete, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, delete: ToDoData, position: Int){
        val snackBar = Snackbar.make(view, "Удалено: '${delete.title}'", Snackbar.LENGTH_LONG)
        snackBar.setAction("Вернуть"){
            toDoViewModel.insertData(delete)
            adapter.notifyItemChanged(position)
        }
        snackBar.show()
    }

    /*private fun showEmptyDatabase(emptyDatabase: Boolean) {
        if (emptyDatabase){
            view?.tv_no_data?.visibility = View.VISIBLE
            view?.img_cloud?.visibility = View.VISIBLE
        }else{
            view?.tv_no_data?.visibility = View.INVISIBLE
            view?.img_cloud?.visibility = View.INVISIBLE
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all -> confirmDeleting()
            R.id.priority_high -> toDoViewModel.sortByHighPriority.observe(viewLifecycleOwner, { adapter.setData(it) })
            R.id.priority_low -> toDoViewModel.sortByLowPriority.observe(viewLifecycleOwner, { adapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String?) {
        val searchQuery = "%$query%"

        toDoViewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, { list ->
            list?.let {
                Log.d("ListFragment", "searchThroughDatabase")
                adapter.setData(it)
            }
        })
    }

    private fun confirmDeleting() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Да") {_,_ ->
            toDoViewModel.deleteAll()
            Toast.makeText(requireContext(), "Всё удалено",
                Toast.LENGTH_SHORT)
                .show()
        }
        builder.setNegativeButton("Нет"){_,_ -> }
        builder.setTitle("Удалить всё?")
        builder.setMessage("Вы точно хотите удалить?")
        builder.create().show()
    }


}