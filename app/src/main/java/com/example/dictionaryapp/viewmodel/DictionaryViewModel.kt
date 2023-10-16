import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dictionaryapp.model.DictionaryModel
import com.example.dictionaryapp.services.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


class DictionaryViewModel : ViewModel() {

    // LiveData to hold the API response data
    private val _dictionaryResponse = MutableLiveData<List<DictionaryModel?>>()
    val dictionaryResponse: MutableLiveData<List<DictionaryModel?>> = _dictionaryResponse

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    // Function to make the API call and update the LiveData
    fun fetchDictionaryData(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loading.postValue(true)


                val response: Response<List<DictionaryModel>> = Api.retrofitServiceMoshi.getWordDictionary(word).execute()

                if (response.isSuccessful) {
                    println(response.message())
                    println(response.body())
                    println(response.code())
                    println(response.raw())
                    val body = response.body()
                    if (body != null) {
                        // Update the LiveData with the response data
                        println(body[0].toMap())
                        _dictionaryResponse.postValue(body)
                    }
                    _loading.postValue(false)
                }
                println(response.message())
                println(response.body())
                println(response.code())
                println(response.raw())// Handle other cases like errors, etc.
            } catch (e: Exception) {

                println(e.printStackTrace())
                // Handle network errors
            }finally{_loading.postValue(false)}


        }
    }
}

