package kg.timur.app.todo.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kg.timur.app.todo.R
import kg.timur.app.todo.data.models.Priority
import kg.timur.app.todo.data.models.ToDoData
import kg.timur.app.todo.data.viewmodel.ToDoViewModel
import kg.timur.app.todo.fragments.ShareViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*


class AddFragment : Fragment() {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mShareViewModel: ShareViewModel by viewModels()
    


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add, container, false)

        setHasOptionsMenu(true)

        view.spinner.onItemSelectedListener = mShareViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add){
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val mTitle = et_title.text.toString()
        val mPriority = spinner.selectedItem.toString()
        val mDescription = et_description.text.toString()

        val validation = mShareViewModel.verifyDataFromUser(mTitle, mDescription)
        if (validation){
            val newData = ToDoData(0, mTitle, mShareViewModel.parsePriority(mPriority), mDescription)
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }
}