package com.flicker.sampleapp.repository

import android.util.Log
import com.flicker.sampleapp.domain.model.Photo
import com.flicker.sampleapp.network.CustomNetworkCall
import com.flicker.sampleapp.network.FlickerPhotoService
import com.flicker.sampleapp.network.GenericNetworkResponse
import com.flicker.sampleapp.network.model.PhotoDtoMapper

class FlickerPhotoRepositoryImpl(
    private val photoService: FlickerPhotoService,
    private val photoDtoMapper: PhotoDtoMapper,
    private val method: String,
    private val apiKey: String,
    private val format: String,
    private val noJsonCallback: Int,
    private val perPage: Int
) : FlickerPhotoRepository {
    override suspend fun search(
        text: String,
        page: Int
    ): GenericNetworkResponse<List<Photo>> {

        val response = CustomNetworkCall.safeApiCall {
            photoService.search(
                method,
                apiKey,
                text,
                format,
                noJsonCallback,
                perPage,
                page
            )
        }
        return when (response) {
            is GenericNetworkResponse.Success -> {
                GenericNetworkResponse.Success(photoDtoMapper.toDomainList(response.value.photoSearchResponse.photos))
            }
            is GenericNetworkResponse.GenericError -> {
                response
            }
        }
    }
}