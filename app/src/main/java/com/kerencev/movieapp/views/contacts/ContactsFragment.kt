package com.kerencev.movieapp.views.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.kerencev.movieapp.R
import com.kerencev.movieapp.databinding.ContactsFragmentBinding
import com.kerencev.movieapp.model.extensions.showToast

class ContactsFragment : Fragment() {
    private var _binding: ContactsFragmentBinding? = null
    private val binding get() = _binding!!
    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getContacts()
            } else {
                requireContext().showToast(getString(R.string.need_permission_to_reed_contacts))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun checkPermission() {
        context?.let { context ->
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) -> {
                    getContacts()
                }
                else -> {
                    requestPermission()
                }
            }

        }
    }

    private fun requestPermission() {
        permissionResult.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun getContacts() {
        context?.let { context ->
            val cursorWithContacts: Cursor? = context.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val columnIndex =
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        if (columnIndex >= 0) {
                            val name = cursor.getString(columnIndex)
                            addView(name)
                        }
                    }
                }
            }
            cursorWithContacts?.close()
        }
    }

    private fun addView(name: String?) = with(binding) {
        linearContacts.addView(TextView(requireContext()).apply {
            text = name
        })
    }
}