package com.erros.minimax.fenix.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by milkman on 07.02.18.
 */
data class Person(
        @SerializedName("id") val id: Int, //1
        @SerializedName("name") val name: String, //Leanne Graham
        @SerializedName("username") val username: String, //Bret
        @SerializedName("email") val email: String, //Sincere@april.biz
        @SerializedName("address") val address: Address?,
        @SerializedName("phone") val phone: String, //1-770-736-8031 x56442
        @SerializedName("website") val website: String, //hildegard.org
        @SerializedName("company") val company: Company?
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: "",
            source.readParcelable<Address>(Address::class.java.classLoader),
            source.readString() ?: "",
            source.readString() ?: "",
            source.readParcelable<Company>(Company::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(name)
        writeString(username)
        writeString(email)
        writeParcelable(address, 0)
        writeString(phone)
        writeString(website)
        writeParcelable(company, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Person> = object : Parcelable.Creator<Person> {
            override fun createFromParcel(source: Parcel): Person = Person(source)
            override fun newArray(size: Int): Array<Person?> = arrayOfNulls(size)
        }
    }
}

data class Company(
        @SerializedName("name") val name: String, //Romaguera-Crona
        @SerializedName("catchPhrase") val catchPhrase: String, //Multi-layered client-server neural-net
        @SerializedName("bs") val bs: String //harness real-time e-markets
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: ""
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(catchPhrase)
        writeString(bs)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Company> = object : Parcelable.Creator<Company> {
            override fun createFromParcel(source: Parcel): Company = Company(source)
            override fun newArray(size: Int): Array<Company?> = arrayOfNulls(size)
        }
    }
}

data class Address(
        @SerializedName("street") val street: String, //Kulas Light
        @SerializedName("suite") val suite: String, //Apt. 556
        @SerializedName("city") val city: String, //Gwenborough
        @SerializedName("zipcode") val zipcode: String, //92998-3874
        @SerializedName("geo") val geo: Geo?
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: "",
            source.readParcelable<Geo>(Geo::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(street)
        writeString(suite)
        writeString(city)
        writeString(zipcode)
        writeParcelable(geo, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Address> = object : Parcelable.Creator<Address> {
            override fun createFromParcel(source: Parcel): Address = Address(source)
            override fun newArray(size: Int): Array<Address?> = arrayOfNulls(size)
        }
    }
}

data class Geo(
        @SerializedName("lat") val lat: String, //-37.3159
        @SerializedName("lng") val lng: String //81.1496
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString() ?: "",
            source.readString() ?: ""
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(lat)
        writeString(lng)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Geo> = object : Parcelable.Creator<Geo> {
            override fun createFromParcel(source: Parcel): Geo = Geo(source)
            override fun newArray(size: Int): Array<Geo?> = arrayOfNulls(size)
        }
    }
}

data class Post(
        @SerializedName("id") val id: Int,
        @SerializedName("userId") val userId: Int,
        @SerializedName("title") val title: String,
        @SerializedName("body") val body: String
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt(),
            source.readString() ?: "",
            source.readString() ?: ""
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeInt(userId)
        writeString(title)
        writeString(body)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Post> = object : Parcelable.Creator<Post> {
            override fun createFromParcel(source: Parcel): Post = Post(source)
            override fun newArray(size: Int): Array<Post?> = arrayOfNulls(size)
        }
    }
}