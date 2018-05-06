package com.erros.minimax.fenix.data

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by milkman on 07.02.18.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Person(
        @SerializedName("id") val id: Int, //1
        @SerializedName("name") val name: String, //Leanne Graham
        @SerializedName("username") val username: String, //Bret
        @SerializedName("email") val email: String, //Sincere@april.biz
        @SerializedName("address") val address: Address,
        @SerializedName("phone") val phone: String, //1-770-736-8031 x56442
        @SerializedName("website") val website: String, //hildegard.org
        @SerializedName("company") val company: Company
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Company(
        @SerializedName("name") val name: String, //Romaguera-Crona
        @SerializedName("catchPhrase") val catchPhrase: String, //Multi-layered client-server neural-net
        @SerializedName("bs") val bs: String //harness real-time e-markets
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Address(
        @SerializedName("street") val street: String, //Kulas Light
        @SerializedName("suite") val suite: String, //Apt. 556
        @SerializedName("city") val city: String, //Gwenborough
        @SerializedName("zipcode") val zipcode: String, //92998-3874
        @SerializedName("geo") val geo: Geo
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Geo(
        @SerializedName("lat") val lat: String, //-37.3159
        @SerializedName("lng") val lng: String //81.1496
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Post(
        @SerializedName("id") val id: Int,
        @SerializedName("userId") val userId: Int,
        @SerializedName("title") val title: String,
        @SerializedName("body") val body: String
) : Parcelable