package com.zj.weather.view.city

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.zj.weather.R
import com.zj.utils.lce.NoContent
import com.zj.model.room.entity.CityInfo
import com.zj.weather.view.city.viewmodel.CityListViewModel
import com.zj.weather.view.city.widget.CityListItem
import com.zj.weather.view.city.widget.TitleBar

@Composable
fun CityListPage(
    cityListViewModel: CityListViewModel,
    onBack: () -> Unit
) {
    val cityInfoList by cityListViewModel.cityInfoList.observeAsState(listOf())
    CityListPage(
        cityInfoList = cityInfoList, onBack = onBack, toWeatherDetails = {
            cityListViewModel.updateCityInfoIndex(it)
            onBack()
        }
    ) {
        cityListViewModel.deleteCityInfo(it)
    }
}

@Composable
fun CityListPage(
    cityInfoList: List<CityInfo>,
    onBack: () -> Unit,
    toWeatherDetails: (CityInfo) -> Unit,
    deleteCityInfo: (CityInfo) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.page_margin))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // 标题栏
        TitleBar(R.string.city_title, onBack)
        if (cityInfoList.isNotEmpty()) {
            val listState = rememberLazyListState()
            LazyColumn(state = listState) {
                items(cityInfoList) { cityInfo ->
                    CityListItem(
                        cityInfo,
                        cityInfo.isLocation != 1,
                        toWeatherDetails, deleteCityInfo
                    )
                }
            }
        } else {
            NoContent(tip = stringResource(id = R.string.add_location_warn2))
        }
    }
}

@Preview(showBackground = false, name = "城市列表")
@Composable
fun CityListPagePreview() {
    val cityInfo = CityInfo(
        name = "朱江",
        province = "微子国",
        city = "南街"
    )
    val cityList = arrayListOf<CityInfo>()
    for (index in 0..10) {
        cityList.add(cityInfo)
    }
    CityListPage(cityInfoList = cityList, {}, {}, {})
}
