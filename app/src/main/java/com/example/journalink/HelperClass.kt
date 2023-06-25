package com.example.journalink

class HelperClass {
    private var name: String? = null
    private var email: String? = null
    private var username: String? = null
    private var password: String? = null

    constructor(name: String?, email: String?, username: String?, password: String?) {
        this.name = name
        this.email = email
        this.username = username
        this.password = password
    }

    constructor() {}
}
