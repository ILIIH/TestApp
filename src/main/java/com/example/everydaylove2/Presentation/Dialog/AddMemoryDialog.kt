package com.example.everydaylove2.Presentation.Dialog

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView.OnDateChangeListener
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.everydaylove2.Framework.DAOMemories
import com.example.everydaylove2.Presentation.Helpers.DataTransformation
import com.example.everydaylove2.databinding.DialorgMemoryAddBinding
import com.example.everydaylove2.di.MyApplication
import com.example.everydaylove2.domain.Memory
import com.example.everydaylove2.domain.baceImg
import com.google.firebase.storage.StorageReference
import java.util.*
import javax.inject.Inject

class AddMemoryDialog : DialogFragment() {

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        (activity.applicationContext as MyApplication).appComponent.dialogInject(this)
    }

    @Inject
    lateinit var StorageReference: StorageReference
    @Inject
    lateinit var DatabaceMemory: DAOMemories

    lateinit var view: DialorgMemoryAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = DialorgMemoryAddBinding.inflate(inflater)
        var url = Uri.parse(requireArguments().getString("image"))
        view.ImageToDownload.setImageURI(url)

        DatabaceMemory.ReadAll()

        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val month: Int = calendar.get(Calendar.MONTH)

        var curmonth: String = month.toString()
        var curday: String = day.toString()
        var curyear: String = year.toString()

        view.calendarView.setOnDateChangeListener(
            OnDateChangeListener { view, year, month, dayOfMonth ->
                curmonth = month.toString()
                curday = dayOfMonth.toString()
                curyear = year.toString()
            }
        )

        view.SubmitButton.setOnClickListener {

            SetLoadIcon(view, true)

            DatabaceMemory.loaded.observe(this) { it ->
                if (it == true) uploadPicture(url, view.DescriptionText.text.toString(), curmonth, curday, curyear)
            }
        }

        return view.root
    }

    private fun uploadPicture(uri: Uri, description: String, month: String, day: String, year: String) {

        DatabaceMemory.loaded.postValue(false)

        var key = baceImg + (DatabaceMemory.memories.size + 1).toString()

        DatabaceMemory.add(Memory(key, description, year, DataTransformation(month, day))).addOnFailureListener {
            Toast.makeText(requireContext(), "Воспоминание не загружено : ${it.message}", Toast.LENGTH_LONG).show()
        }

        val mountainImagesRef: StorageReference = StorageReference.child("images/$key.jpg")

        mountainImagesRef.putFile(uri).addOnSuccessListener {
            dismiss()
            Toast.makeText(requireContext(), "Воспоминание успешно загружено", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Воспоминание не загружено : ${it.message}", Toast.LENGTH_LONG).show()
            SetLoadIcon(view, false)
        }
    }
    fun SetLoadIcon(view: DialorgMemoryAddBinding, load: Boolean) {
        if (load) {
            view.progressBar.visibility = View.VISIBLE
            view.calendarView.visibility = View.GONE
            view.SubmitButton.visibility = View.GONE
            view.Title.visibility = View.INVISIBLE
            view.ImageToDownload.visibility = View.GONE
            view.DescriptionText.visibility = View.GONE
            view.DescriptionLayout.visibility = View.GONE
        } else {
            view.progressBar.visibility = View.GONE
            view.calendarView.visibility = View.VISIBLE
            view.SubmitButton.visibility = View.VISIBLE
            view.Title.visibility = View.INVISIBLE
            view.ImageToDownload.visibility = View.VISIBLE
            view.DescriptionText.visibility = View.VISIBLE
            view.DescriptionLayout.visibility = View.VISIBLE
        }
    }
}
