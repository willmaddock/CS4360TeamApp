package com.example.cs4360app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.cs4360app.R
import com.example.cs4360app.activities.LocationData
import androidx.core.text.HtmlCompat


class LocationExpandableAdapter(
    private val context: Context,
    private val locationData: List<LocationData>
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return locationData.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1 // Each location item has only one expandable child
    }

    override fun getGroup(groupPosition: Int): Any {
        return locationData[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return locationData[groupPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.location_list_group, null)
        }

        // Set the location name for the group view (header)
        val locationName: TextView = view!!.findViewById(R.id.title_location_name)
        locationName.text = locationData[groupPosition].name

        return view
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup?
    ): View {
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.location_list_child, null)
        }

        // Accessing the data for the specific location
        val location = locationData[groupPosition]

        // Set banner image, cost, address, and recommendations
        val bannerImage: ImageView = view!!.findViewById(R.id.banner_image)
        val costText: TextView = view.findViewById(R.id.cost_text)
        val addressText: TextView = view.findViewById(R.id.address_text)
        val recommendationsText: TextView = view.findViewById(R.id.recommendations_text)

        bannerImage.setImageResource(location.imageResourceId)
        costText.text = HtmlCompat.fromHtml("<b>Cost before 5pm:</b> ${location.costBefore5pm}", HtmlCompat.FROM_HTML_MODE_LEGACY)
        addressText.text = HtmlCompat.fromHtml("<b>Address:</b> ${location.address}", HtmlCompat.FROM_HTML_MODE_LEGACY)
        recommendationsText.text = HtmlCompat.fromHtml("<b>Recommendations:</b> ${location.recommendations}", HtmlCompat.FROM_HTML_MODE_LEGACY)

        return view
    }

}
