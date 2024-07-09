package com.deora.cms_app.listener

import com.deora.cms_app.contact.Contact

interface ListAction {
    fun onClick(contact: Contact)
}