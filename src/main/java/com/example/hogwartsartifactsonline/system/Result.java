package com.example.hogwartsartifactsonline.system;

import lombok.Getter;
import lombok.Setter;

/**
 * {
 *   "flag": true,
 *   "code": 200,
 *   "message": "Find One Success",
 *   "data": {
 *     "id": "1250808601744904192",
 *     "name": "Invisibility Cloak",
 *     "description": "An invisibility cloak is used to make the wearer invisible.",
 *     "imageUrl": "ImageUrl",
 *     "owner": {
 *       "id": 2,
 *       "name": "Harry Potter",
 *       "numberOfArtifacts": 2
 *     }
 *   }
 * }
 */
@Getter
@Setter
public class Result {

    private boolean flag;
    private int code;
    private String message;
    private Object data;

    public Result(boolean flag, int code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(boolean flag, int code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }
}
