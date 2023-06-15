package com.example.listed.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import com.example.listed.databinding.ActivityMainBinding
import com.example.listed.databinding.FragmentLinksBinding
import com.example.listed.ui.activity.AllLinksActivity
import com.example.listed.ui.activity.MainActivity
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

    private var _binding: FragmentLinksBinding?=null
    private val binding get()=_binding!!

    private val homePageViewModel: HomePageViewModel by viewModels()
    private var linksList: ArrayList<TopLinksItem?>? = ArrayList()
    private lateinit var linksAdapter: LinksAdapter

    private var clickDates: ArrayList<String> = ArrayList()
    private lateinit var dashboardData: DashboardResponseModel
    val entries: MutableList<Entry> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        _binding=FragmentLinksBinding.inflate(inflater,container,false);
        
        linksAdapter = LinksAdapter(linksList, object : CopyListener {
            override fun linkClick(link: String) {
                saveToClipboard(this@LinksFragment.requireContext(), link)
            }
        })
        binding.linksRecycler.layoutManager = LinearLayoutManager(this@LinksFragment.requireContext())
        binding.linksRecycler.adapter = linksAdapter
        binding.greet.text = homePageViewModel.getGreetings()

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
        binding.linkTabs.setOnCheckedStateChangeListener { group, checkedIds ->
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
        binding.viewAllLiinks.setOnClickListener {
            when(binding.linkTabs.checkedChipId ){
                R.id.top_links -> {
                    val intent= Intent(requireContext(), AllLinksActivity::class.java)
                    intent.putParcelableArrayListExtra(MainActivity.LINKS_DATA_MSG, dashboardData.data?.top_links)
                    startActivity(intent)
                }
                R.id.recent_links -> {
                    val intent= Intent(requireContext(), AllLinksActivity::class.java)
                    intent.putParcelableArrayListExtra(MainActivity.LINKS_DATA_MSG, dashboardData.data?.recent_links)
                    startActivity(intent)
                }
            }
        }

        return binding.root;
    }

    private fun bindDataToHomePage(dashboardData: DashboardResponseModel) {
        binding.todaysClicks.text = dashboardData.today_clicks.toString()
        binding.topLocation.text = dashboardData.top_location.toString()
        binding.topSource.text = dashboardData.top_source.toString()
        binding.linkTabs.check(R.id.top_links)
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

        val xAxis: XAxis = binding.clicksGraph.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = resources.getColor(R.color.grey_color)
        xAxis.setDrawGridLines(true)
        xAxis.gridColor = resources.getColor(R.color.graph_grid_color)
        xAxis.gridLineWidth = 1.2f
        xAxis.valueFormatter =
            IndexAxisValueFormatter(clickDates) // Custom formatter for x-axis labels

        val rYAxis = binding.clicksGraph.axisRight
        rYAxis.isEnabled = false


        val lYAxis = binding.clicksGraph.axisLeft
        lYAxis.labelCount = 5
        lYAxis.spaceTop = 0f
        lYAxis.spaceBottom = 0f
        lYAxis.textColor = resources.getColor(R.color.grey_color)
        lYAxis.setDrawGridLines(true)
        lYAxis.setDrawAxisLine(false)
        lYAxis.gridColor = resources.getColor(R.color.graph_grid_color)
        lYAxis.gridLineWidth = 1.2f


        binding.clicksGraph.description.isEnabled = false
        binding.clicksGraph.legend.isEnabled = false
        binding.clicksGraph.setTouchEnabled(false)
        binding.clicksGraph.isDragEnabled = false
        binding.clicksGraph.setScaleEnabled(false)
        binding.clicksGraph.setPinchZoom(false)
        binding.clicksGraph.isDoubleTapToZoomEnabled = false
        binding.clicksGraph.isHighlightPerTapEnabled = false
        binding.clicksGraph.isHighlightPerDragEnabled = false
        binding.clicksGraph.animateX(1000)


        binding.clicksGraph.data = lineData
        binding.clicksGraph.invalidate()
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
        binding.progress.isVisible = true;
    }

    private fun hideProgress() {
        binding.progress.isVisible = false
    }

}


