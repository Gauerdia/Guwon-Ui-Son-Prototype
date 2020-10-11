package szymendera_development.guwon_app.Utils

import java.util.ArrayList
import ImageModel

/**
 * Created by szymendera on 05.12.2018.
 */
// returns the Image-Array for the Diashow on the first site
public fun populateList(myImageList: IntArray): ArrayList<ImageModel> {

    val list = ArrayList<ImageModel>()

    for (i in 0..5) {
        val imageModel = ImageModel()
        imageModel.setImage_drawables(myImageList[i])
        list.add(imageModel)
    }

    return list
}