package kg.timur.app.todo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kg.timur.app.todo.R
import kg.timur.app.todo.data.models.Priority
import kg.timur.app.todo.data.models.ToDoData
import kg.timur.app.todo.data.viewmodel.ToDoViewModel
import kg.timur.app.todo.fragments.ShareViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import kotlinx.android.synthetic.main.row_layout.view.*


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val shareViewModel: ShareViewModel by viewModels()
    private val toDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_update, container, false)

        setHasOptionsMenu(true )

        view.et_title_update.setText(args.currentitem.title)
        view.et_description_update.setText(args.currentitem.description)
        view.spinner_update.setSelection(shareViewModel.parsePriorityToInt(args.currentitem.priority))
        view.spinner_update.onItemSelectedListener = shareViewModel.listener
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        val title = et_title_update.text.toString()
        val description = et_description_update.text.toString()
        val priority = spinner_update.selectedItem.toString()
        val validation = shareViewModel.verifyDataFromUser(title, description)
        if (validation){
            val updateItem = ToDoData(
                args.currentitem.id,
                title,
                shareViewModel.parsePriority(priority),
                description
            )
            toDoViewModel.updateData(updateItem)
            Toast.makeText(requireContext(), "Успешно обновлено", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Заполните поля", Toast.LENGTH_SHORT).show()

        }
    }

    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Да") {_,_ ->
            toDoViewModel.delete(args.currentitem)
            Toast.makeText(requireContext(), "Удалено: ${args.currentitem.title}",
                Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("Нет"){_,_ -> }
        builder.setTitle("Удалить ${args.currentitem.title}?")
        builder.setMessage("Вы точно хотите удалить ${args.currentitem.title}?")
        builder.create().show()
    }
}