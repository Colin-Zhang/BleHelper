package com.colinzhang.blehelperlibrary

import java.io.Serializable

/**
 * Created by Sefon-1476 on 2018/8/31.
 */
internal class BleUUID : Serializable {
    var ServiceUUID: String? = null
    var CharacterUUID_WRITE: String? = null
    var CharacterUUID_NOTIFY: String? = null
    var CharacterUUID_READ: String? = null
}