package br.com.wakim.github.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel

@PaperParcel
data class Repository(val name: String?,
                      val id: Long,
                      @SerializedName("full_name") val fullName: String?,
                      val description: String?,
                      @SerializedName("html_url") val htmlUrl: String?,
                      @SerializedName("stargazers_count") val stargazersCount: Int,
                      @SerializedName("watchers_count") val watchersCount: Int,
                      @SerializedName("forks_count") val forksCount: Int,
                      @SerializedName("open_issues_count") val openIssuesCount: Int,
                      val language: String?) : Parcelable {

    companion object {
        @JvmField val CREATOR = PaperParcelRepository.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelRepository.writeToParcel(this, dest, flags)
    }
}