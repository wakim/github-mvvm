package br.com.wakim.github.data.model

import android.os.Parcel
import android.os.Parcelable
import paperparcel.PaperParcel

@PaperParcel
data class NextPage(val hasMore: Boolean = false,
                    val page: Int = 1) : Parcelable {

    companion object {
        @JvmField val CREATOR = PaperParcelNextPage.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelNextPage.writeToParcel(this, dest, flags)
    }
}