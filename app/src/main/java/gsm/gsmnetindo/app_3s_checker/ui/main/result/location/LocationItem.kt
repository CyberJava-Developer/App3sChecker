package gsm.gsmnetindo.app_3s_checker.ui.main.result.location

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.Location
import kotlinx.android.synthetic.main.item_location.*

class LocationItem(
    private val location: Location
): Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            location_coba.text = "${location.latitude} - ${location.longitude}\n${location.createdAt}\n${location.updatedAt}"
        }
    }

    override fun getLayout() = R.layout.item_location
}