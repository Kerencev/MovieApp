package com.kerencev.movieapp.views.dialogfragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.kerencev.movieapp.R
import com.kerencev.movieapp.data.database.entities.NoteEntity
import com.kerencev.movieapp.viewmodels.DetailsViewModel
import com.kerencev.movieapp.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

//TODO Сделать так, что при отображении звёзд, если рейтинг уже был по клику на сохранение сохранялся актуальный рейтинг

class NoteDialogFragment : DialogFragment(), CoroutineScope by MainScope() {
    private val noteViewModel: NoteViewModel by viewModel()
    private lateinit var editText: AppCompatEditText
    private lateinit var arrOfStars: Array<ImageView?>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val id = arguments?.getString(BUNDLE_ID)
        val customDialog: View = layoutInflater.inflate(R.layout.dialig_fragment_note, null)
        editText = customDialog.findViewById(R.id.edit_text)
        val btnSave = customDialog.findViewById<TextView>(R.id.action_save)
        val btnCancel = customDialog.findViewById<TextView>(R.id.action_cancel)
        arrOfStars = arrayOfNulls(10)
        initArrOfStars(arrOfStars, customDialog)
        setStarsClickListener()

        btnSave.setOnClickListener {
            id?.let { id ->
                //TODO разобраться где запускат корутины, что бы отображать рейтинг в DetailsFragment
                launch(Dispatchers.IO) {
                    noteViewModel.saveNoteInDataBase(id, editText.text.toString())
                    setFragmentResult(id)
                }
            }
            dismiss()
        }
        btnCancel.setOnClickListener { dismiss() }

        val dataObserver = Observer<NoteEntity?> { renderData(it) }
        noteViewModel.noteData.observe(this, dataObserver)
        id?.let { noteViewModel.getNote(it) }

        val builder = AlertDialog.Builder(requireContext())
            .setView(customDialog)
        return builder.create()
    }

    private fun setFragmentResult(id: String) {
        val bundle = Bundle()
        bundle.putString(BUNDLE_NOTE_ID, id)
        parentFragmentManager.setFragmentResult(RESULT_SAVED_RATING, bundle)
    }

    private fun renderData(noteEntity: NoteEntity?) {
        when (noteEntity) {
            null -> return
            else -> {
                if (noteEntity.rating > 0) {
                    changeColorOfStars(noteEntity.rating - 1)
                }
                editText.setText(noteEntity.note)
            }
        }
    }

    private fun setStarsClickListener() {
        arrOfStars[0]?.setOnClickListener {
            when {
                noteViewModel.getRating() == 0 -> {
                    noteViewModel.setRating(1)
                    changeColorOfStars(0)
                }
                else -> {
                    noteViewModel.setRating(0)
                    arrOfStars.forEach {
                        it?.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_star_border_24))
                    }

                }
            }
        }
        for (i in 1 until arrOfStars.size) {
            arrOfStars[i]?.setOnClickListener {
                noteViewModel.setRating(i + 1)
                changeColorOfStars(i)
            }
        }
    }

    private fun changeColorOfStars(count: Int) {
        for (i in 0..count) {
            arrOfStars[i]?.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_star_24))
        }
        for (i in count + 1 until arrOfStars.size) {
            arrOfStars[i]?.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_star_border_24))
        }
    }

    private fun initArrOfStars(arrOfStars: Array<ImageView?>, customDialog: View) {
        arrOfStars[0] = customDialog.findViewById(R.id.star1)
        arrOfStars[1] = customDialog.findViewById(R.id.star2)
        arrOfStars[2] = customDialog.findViewById(R.id.star3)
        arrOfStars[3] = customDialog.findViewById(R.id.star4)
        arrOfStars[4] = customDialog.findViewById(R.id.star5)
        arrOfStars[5] = customDialog.findViewById(R.id.star6)
        arrOfStars[6] = customDialog.findViewById(R.id.star7)
        arrOfStars[7] = customDialog.findViewById(R.id.star8)
        arrOfStars[8] = customDialog.findViewById(R.id.star9)
        arrOfStars[9] = customDialog.findViewById(R.id.star10)
    }

    companion object {
        const val BUNDLE_ID = "BUNDLE_ID"
        const val RESULT_SAVED_RATING = "RESULT_SAVED_RATING"
        const val BUNDLE_NOTE_ID = "BUNDLE_ID"


        fun newInstance(id: String): NoteDialogFragment {
            val fragment = NoteDialogFragment()
            val bundle = Bundle()
            bundle.putString(BUNDLE_ID, id)
            fragment.arguments = bundle
            return fragment
        }
    }
}