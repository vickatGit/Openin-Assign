package com.example.listed.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listed.R
import com.example.listed.data.model.DashboardResponseModel
import com.example.listed.data.model.OverallUrlChart
import com.example.listed.data.model.TopLinksItem
import com.example.listed.ui.listener.CopyListener
import com.example.listed.ui.util.DashboardState
import com.example.listed.ui.adapter.LinksAdapter
import com.example.listed.ui.listener.AllLinkListener
import com.example.listed.ui.viewmodel.HomePageViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class LinksFragment : Fragment() {


    private val homePageViewModel: HomePageViewModel by viewModels()
    private lateinit var allLinkListener: AllLinkListener

    private lateinit var greetings: TextView
    private lateinit var userName: TextView
    private lateinit var todayClicks: TextView
    private lateinit var topLocation: TextView
    private lateinit var topSource: TextView
    private lateinit var linkTabs: ChipGroup
    private var linksList: ArrayList<TopLinksItem?>? = ArrayList()
    private lateinit var linksAdapter: LinksAdapter
    private lateinit var linksRecycler: RecyclerView
    private lateinit var lineChart: LineChart
    private lateinit var progress: ProgressBar
    private lateinit var allLinks: Button

    private var clickDates: ArrayList<String> = ArrayList()
    private lateinit var dashboardData: DashboardResponseModel
    val entries: MutableList<Entry> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AllLinkListener) {
            allLinkListener = activity as AllLinkListener
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_links, container, false)
        initialiseViews(view)
        linksAdapter = LinksAdapter(linksList, object : CopyListener {
            override fun linkClick(link: String) {
                saveToClipboard(this@LinksFragment.requireContext(), link)
            }
        })
        linksRecycler.layoutManager = LinearLayoutManager(this@LinksFragment.requireContext())
        linksRecycler.adapter = linksAdapter
        greetings.text = homePageViewModel.getGreetings()

        lifecycleScope.launch {
            homePageViewModel.loadState.collectLatest {
                when (it) {
                    is DashboardState.Loading -> {
                        showProgress()
                    }
                    is DashboardState.Success -> {
                        dashboardData = it.data
                        bindDataToHomePage(dashboardData)
                        hideProgress()

                    }
                    is DashboardState.Error -> {
                        hideProgress()
                    }
                }
            }
        }
        homePageViewModel.getHomePageData()
        homePageViewModel.getGreetings()
        linkTabs.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.contains(R.id.top_links)) {
                linksList?.clear()
                val list = dashboardData.data?.top_links
                list?.let {
                    if(it.size>=4) {
                        linksList?.addAll(it.subList(0, 4))
                    }else{
                        linksList?.addAll(it)
                    }
                    linksAdapter.notifyDataSetChanged()
                    linksAdapter.filter.filter("")
                }

            } else if (checkedIds.contains(R.id.recent_links)) {
                linksList?.clear()
                val list = dashboardData.data?.recent_links
                list?.let {
                    if(it.size>=4) {
                        linksList?.addAll(it.subList(0, 4))
                    }else{
                        linksList?.addAll(it)
                    }
                    linksAdapter.notifyDataSetChanged()
                    linksAdapter.filter.filter("")
                }
            }
        }
        allLinks.setOnClickListener {
            when(linkTabs.checkedChipId ){
                R.id.top_links -> {
                    allLinkListener.onClick(dashboardData.data?.top_links)
                }
                R.id.recent_links -> {
                    allLinkListener.onClick(dashboardData.data?.recent_links)
                }
            }
        }

        return view;
    }

    private fun bindDataToHomePage(dashboardData: DashboardResponseModel) {
        todayClicks.text = dashboardData.today_clicks.toString()
        topLocation.text = dashboardData.top_location.toString()
        topSource.text = dashboardData.top_source.toString()
        linkTabs.check(R.id.top_links)
        modifyChartData(dashboardData.data?.overall_url_chart)
        initialiseChart()


    }

    private fun initialiseChart() {
        val dataSet = LineDataSet(entries, "")
        val startColor = resources.getColor(R.color.blue)
        val endColor = resources.getColor(R.color.transparent)
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(startColor, endColor)
        )


        dataSet.color = resources.getColor(R.color.blue) // Set the line color
        dataSet.lineWidth = 2f
        dataSet.setDrawCircles(false)
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawFilled(true)
        dataSet.fillDrawable = gradientDrawable
        dataSet.setDrawValues(false) // Hide data values on the chart

        // Create a list of LineDataSets and add the LineDataSet to it
        val dataSets: MutableList<ILineDataSet> = mutableListOf()
        dataSets.add(dataSet)

        val lineData = LineData(dataSets)

        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = resources.getColor(R.color.grey_color)
        xAxis.setDrawGridLines(true)
        xAxis.gridColor = resources.getColor(R.color.graph_grid_color)
        xAxis.gridLineWidth = 1.2f
        xAxis.valueFormatter =
            IndexAxisValueFormatter(clickDates) // Custom formatter for x-axis labels

        val rYAxis = lineChart.axisRight
        rYAxis.isEnabled = false


        val lYAxis = lineChart.axisLeft
        lYAxis.labelCount = 5
        lYAxis.spaceTop = 0f
        lYAxis.spaceBottom = 0f
        lYAxis.textColor = resources.getColor(R.color.grey_color)
        lYAxis.setDrawGridLines(true)
        lYAxis.setDrawAxisLine(false)
        lYAxis.gridColor = resources.getColor(R.color.graph_grid_color)
        lYAxis.gridLineWidth = 1.2f


        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.setPinchZoom(false)
        lineChart.isDoubleTapToZoomEnabled = false
        lineChart.isHighlightPerTapEnabled = false
        lineChart.isHighlightPerDragEnabled = false
        lineChart.animateX(1000)


        lineChart.data = lineData
        lineChart.invalidate()
    }

    private fun modifyChartData(overallUrlChart: OverallUrlChart?) {
        overallUrlChart.let { chart ->
            chart?.javaClass?.declaredFields?.forEachIndexed { index, field ->
                field.isAccessible = true
                val date = field.name
                clickDates.add(formatDate(date))
                val value = field.get(chart) as? Int
                val entry = Entry(index.toFloat(), value?.toFloat() ?: 0f)
                entries.add(entry)
            }
        }

    }


    fun saveToClipboard(context: Context, text: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(
            this@LinksFragment.requireContext(),
            "Smart Link copied to clipboard",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun initialiseViews(view: View) {
        greetings = view.findViewById(R.id.greet)
        userName = view.findViewById(R.id.user_name)
        topLocation = view.findViewById(R.id.top_location)
        todayClicks = view.findViewById(R.id.todays_clicks)
        topSource = view.findViewById(R.id.top_source)
        linkTabs = view.findViewById(R.id.link_tabs)
        linksRecycler = view.findViewById(R.id.links_recycler)
        lineChart = view.findViewById(R.id.clicks_graph)
        progress = view.findViewById(R.id.progress)
        allLinks = view.findViewById(R.id.view_all_liinks)

    }

    private fun formatDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        var outputDate: String = try {
            val date = inputFormat.parse(date)
            outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            "N/A"
        }
        return outputDate
    }

    private fun showProgress() {
        progress.isVisible = true;
    }

    private fun hideProgress() {
        progress.isVisible = false
    }

}


