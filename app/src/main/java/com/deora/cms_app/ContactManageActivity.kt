package com.deora.cms_app

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deora.cms_app.contact.Contact
import com.deora.cms_app.contact.Request
import com.deora.cms_app.databinding.ActivityContactManageBinding
import com.deora.cms_app.network.NetworkClient
import com.deora.cms_app.utils.Constant
import com.deora.cms_app.utils.DataProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactManageActivity : AppCompatActivity(), Callback<com.deora.cms_app.contact.Response> {
    private lateinit var binding: ActivityContactManageBinding
    private var uContact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactManageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        uContact = DataProvider.contact

        if (uContact != null) {
            binding.edName.setText(uContact?.name)
            binding.edMobileNo.setText(uContact?.mobileNo)
            binding.edEmail.setText(uContact?.email)
        }

        binding.btnContact.setOnClickListener {
            handleContact()
        }
    }

    private fun handleContact() {
        val name = binding.edName.getUIText()
        val mobileNo = binding.edMobileNo.getUIText()
        val email = binding.edEmail.getUIText()

        if (name.isNotEmpty() && mobileNo.isNotEmpty() && email.isNotEmpty()) {
            val contact = Contact(id = uContact?.id ?: "", name = name, mobileNo = mobileNo, email = email)

            val request = Request(
                action = if (uContact != null) Constant.UPDATE_CONTACT else Constant.ADD_CONTACT,
                id = contact.id,
                name = contact.name,
                mobileNo = contact.mobileNo,
                email = contact.email
            )

            val callResponse = NetworkClient.api.makeApiCall(request)
            callResponse.enqueue(this)
        } else {
            showToastMessage("Please fill contact information")
        }
    }

    override fun onResponse(call: Call<com.deora.cms_app.contact.Response>, response: Response<com.deora.cms_app.contact.Response>) {
        if (response.isSuccessful && response.body() != null) {
            val serverResponse = response.body()!!
            if (serverResponse.status) {
                showToastMessage(serverResponse.message)
                finish()
            } else {
                showToastMessage(serverResponse.message)
            }
        } else {
            showToastMessage("Failed to save contact")
        }
    }

    override fun onFailure(call: Call<com.deora.cms_app.contact.Response>, t: Throwable) {
        showToastMessage("Server is not responding. Please contact your system administrator.")
    }

    private fun EditText.getUIText() = this.text.toString().trim()

    private fun showToastMessage(message: String) {
        Toast.makeText(this@ContactManageActivity, message, Toast.LENGTH_SHORT).show()
    }
}
