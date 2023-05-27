package com.example.listed.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class DashboardResponseModel(
	val top_location: String? = null,
	val data: Data? = null,
	val message: String? = null,
	val extra_income: Any? = null,
	val total_links: Int? = null,
	val top_source: String? = null,
	val total_clicks: Int? = null,
	val start_time: String? = null,
	val applied_campaign: Int? = null,
	val support_whatsapp_number: String? = null,
	val today_clicks: Int? = null,
	val status: Boolean? = null,
	val statusCode: Int? = null,
	val links_created_today: Int? = null
)

data class RecentLinksItem(
	val app: String? = null,
	val thumbnail: Any? = null,
	val smart_link: String? = null,
	val created_at: String? = null,
	val url_id: Int? = null,
	val web_link: String? = null,
	val title: String? = null,
	val times_ago: String? = null,
	val url_prefix: String? = null,
	val domain_id: String? = null,
	val url_suffix: String? = null,
	val original_image: String? = null,
	val total_clicks: Int? = null
)

@Parcelize
data class TopLinksItem(
	val app: String? = null,
	val thumbnail: String? = null,
	val smart_link: String? = null,
	val created_at: String? = null,
	val url_id: Int? = null,
	val web_link: String? = null,
	val title: String? = null,
	val times_ago: String? = null,
	val url_prefix: String? = null,
	val domain_id: String? = null,
	val url_suffix: String? = null,
	val original_image: String? = null,
	val total_clicks: Int? = null
):Parcelable

data class Data(
	val top_links: ArrayList<TopLinksItem?>? = null,
	val recent_links: ArrayList<TopLinksItem?>? = null,
	val overall_url_chart: OverallUrlChart? = null
)

data class OverallUrlChart(
	@SerializedName("2023-04-26") val `2023-04-26`: Int? = null,
	@SerializedName("2023-04-27") val `2023-04-27`: Int? = null,
	@SerializedName("2023-04-28")val `2023-04-28`: Int? = null,
	@SerializedName("2023-04-29")val `2023-04-29`: Int? = null,
	@SerializedName("2023-04-30")val `2023-04-30`: Int? = null,
	@SerializedName("2023-05-01")val `2023-05-01`: Int? = null,
	@SerializedName("2023-05-02") val `2023-05-02`: Int? = null,
	@SerializedName("2023-05-03") val `2023-05-03`: Int? = null,
	@SerializedName("2023-05-04") val `2023-05-04`: Int? = null,
	@SerializedName("2023-05-05") val `2023-05-05`: Int? = null,
	@SerializedName("2023-05-06") val `2023-05-06`: Int? = null,
	@SerializedName("2023-05-07") val `2023-05-07`: Int? = null,
	@SerializedName("2023-05-08") val `2023-05-08`: Int? = null,
	@SerializedName("2023-05-09") val `2023-05-09`: Int? = null,
	@SerializedName("2023-05-10") val `2023-05-10`: Int? = null,
	@SerializedName("2023-05-11") val `2023-05-11`: Int? = null,
	@SerializedName("2023-05-12") val `2023-05-12`: Int? = null,
	@SerializedName("2023-05-13") val `2023-05-13`: Int? = null,
	@SerializedName("2023-05-14") val `2023-05-14`: Int? = null,
	@SerializedName("2023-05-15") val `2023-05-15`: Int? = null,
	@SerializedName("2023-05-16") val `2023-05-16`: Int? = null,
	@SerializedName("2023-05-17") val `2023-05-17`: Int? = null,
	@SerializedName("2023-05-18") val `2023-05-18`: Int? = null,
	@SerializedName("2023-05-19") val `2023-05-19`: Int? = null,
	@SerializedName("2023-05-20") val `2023-05-20`: Int? = null,
	@SerializedName("2023-05-21") val `2023-05-21`: Int? = null,
	@SerializedName("2023-05-22") val `2023-05-22`: Int? = null,
	@SerializedName("2023-05-23") val `2023-05-23`: Int? = null,
	@SerializedName("2023-05-24") val `2023-05-24`: Int? = null,
	@SerializedName("2023-05-25") val `2023-05-25`: Int? = null,
	@SerializedName("2023-05-26") val `2023-05-26`: Int? = null
)
