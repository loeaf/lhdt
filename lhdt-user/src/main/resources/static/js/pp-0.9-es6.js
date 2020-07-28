/**
 * ui와 관련없는 것들
 * es6 버전
 * jquery사용하지 않음
 * typescript버전을 es6로 변환
 * @since
 *  2020-07-xx 바닐라js
 *  2020-07-16  pp와 ppui 분리
 * @author gravity@daumsoft.com
 */



 /**
  * 파일 확장자 enum
  */
const Exts = Object.freeze({
    TEXT : {ext:'.txt'},
    IMAGE : {ext:'.bmp .gif .png .jpg .jpeg'},
    OFFICE : {ext:'.doc .docx .xls .xlsx .ppt .pptx .hwp .hwpx'},
    ZIP : {ext:'.zip .alz .7z'}
});

/**
 * performance platform util js
 */
class pp {
    /**
     * 천단위 콤마 추가
     * @param {string|number} strOrNum
     * @returns {string}
     */
    static addComma(strOrNum) {
        var s = strOrNum;
        if (pp.isEmpty(strOrNum)) {
            return "";
        }
        //
        if ("number" === typeof s) {
            s = s.toString();
        }
        //
        return s.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, "$1,");
    }

    /**
     * 콤마 제거
     * @param {string} str
     * @returns {string}
     */
    static unComma(str) {
        if (pp.isEmpty(str)) {
            return "";
        }
        //
        return str.replace(/,/gi, "");
    }

    /**
     * 문자열(또는 숫자)에 천단위 콤마 추가
     * @param {string|number} strOrNum 문자열 또는 숫자
     */
    static formatNumber(strOrNum) {
        return pp.addComma(strOrNum);
    }


    /**
     * 
     * @param {string|Date} str
     * @param {number} len 4,6,8,10,12,14
     */
    static formatDate(str, len){
        if('string' === typeof(str)){
            let s = str.replace(/-/gi, '')
                        .replace(/ /gi, '')
                        .replace(/:/gi, '');

            //
            let result='';
            if(4 <= len){
                //년
                result = `${s.substring(0,4)}`; 
            }
            //
            if(6 <= len){
                //월
                result += `-${s.substring(4,6)}`;
            }
            //
            if(8 <= len){
                //일
                result += `-${s.substring(6,8)}`;
            }
            //
            if(10 <= len){
                //시
                result += ` ${s.substring(8,10)}`;
            }
            //
            if(12 <= len){
                //분
                result += `:${s.substring(10,12)}`;
            }
            //
            if(14 <= len){
                //초
                result += `:${s.substring(12,14)}`;
            }

            //
            return result;

        }    

        //TODO
        throw Error('not impl');
    }

    /**
     *
     * @param {string|number} strOrNum
     * @param {number} padLength
     * @param {string} padStr
     * @returns {string}
     */
    static lpad(strOrNum, padLength, padStr) {
        var s;
        //
        if ("number" === typeof strOrNum) {
            s = strOrNum.toString();
        } else if ("string" === typeof strOrNum) {
            s = strOrNum;
        } else {
            throw new Error(".lpad - not allowed type");
        }
        //
        if (pp.isEmpty(s)) {
            return "";
        }
        //
        while (s.length < padLength) {
            s = padStr + s;
        }
        //
        return s;
    }

    /**
     * unique한 문자열 생성
     * @param {string|undefined} pre prefix
     * @returns {string}
     */
    static createUid(pre) {
        return (pre ? pre : "UID") + new Date().getTime();
    }

    /**
     * jquery의 $.extend()같은거. source의 key/value를 몽땅 target에 추가
     *          target  source
     * case1    object  object
     * case2    object  Map
     * case3    Map     object
     * case4    Map     Map
     * @param {object|Map} target
     * @param {object|Map} source
     * @returns {object|Map} target의 type에 의해 return type결정됨
     * @history
     *  20200717    Map처리 추가
     */
    static extend(target, source) {
        /**
         * object + object
         * @param {Object} target 
         * @param {Object} source 
         * @returns {Object}
         */
        let _mergeObjectAndObject = (target, source)=>{
            Object.keys(source).forEach((value,index)=>{
                let k = value;
                //
                target[k] = source[k];
            });

            //
            return target;
        };

        /**
         * Object + Map
         * @param {Object} target 
         * @param {Map} sourceMap 
         * @returns {Object}
         */
        let _mergeObjectAndMap = (target, sourceMap)=>{
            sourceMap.forEach((value, key)=>{
                target[key] = value;
            });

            //
            return target;
        };


        /**
         * Map + Object
         * @param {Map} targetMap 
         * @param {Object} source 
         * @returns {Map}
         */
        let _mergeMapAndObject = (targetMap, source)=>{
            Object.keys(source).forEach((value,index)=>{
                //
                let k = value;
                //
                targetMap.set(k, source[k]);
            });

            //
            return targetMap;
        };

        /**
         * Map + Map
         * @param {Map} targetMap 
         * @param {Map} sourceMap 
         * @returns {Map}
         */
        let _mergeMapAndMap = (targetMap, sourceMap)=>{
            sourceMap.forEach((value,key)=>{
                targetMap.set(key, value);
            });
    
            //
            return targetMap;
        };


        if (pp.isNull(target) || pp.isNull(source)) {
            return target;
        }

        //case1
        if(target instanceof Object && source instanceof Object){
            return _mergeObjectAndObject(target, source);
        }

        //case2
        if(target instanceof Object && source instanceof Map){
            return _mergeObjectAndMap(target, source);
        }

        //case3
        if(target instanceof Map && source instanceof Object){
            return _mergeMapAndObject(target, source);
        }

        //case4
        if(target instanceof Map && source instanceof Map){
          return _mergeMapAndMap(target, source);
        }

        //
        throw Error('');
    }

    /**
     * 널인지 여부
     * @param {any} obj 오브젝트
     * @returns {boolean}
     */
    static isNull(obj) {
        if (null === obj) {
            return true;
        }

        //
        if (undefined === obj) {
            return true;
        }

        //
        return false;
    }

    /**
     * not isNull
     * @param {any} obj
     * @returns {boolean}
     */
    static isNotNull(obj) {
        return !pp.isNull(obj);
    }

    /**
     * not isEmpty
     * @param {string|number|Array|undefined}strOrArr
     * @returns {boolean}
     */
    static isNotEmpty(strOrArr) {
        return !pp.isEmpty(strOrArr);
    }

    /**
     * obj가 공백인지 여부
     * @param {string | number | Array<any>|undefined} strOrArr 문자열|배열
     * @returns {boolean}
     */
    static isEmpty(strOrArr) {
        if (pp.isNull(strOrArr)) {
            return true;
        }

        //숫자형은 항상 false
        if ("number" === typeof strOrArr) {
            return false;
        }

        //
        if (Array.isArray(strOrArr)) {
            if (0 === strOrArr.length) {
                return true;
            }
        }

        //
        if ("string" === typeof strOrArr) {
            if (0 === strOrArr.length) {
                return true;
            }
        }

        //
        return false;
    }


    /**
     * str이 한글인지 여부
     * @param {string} str 문자열
     * @returns {boolean} true(한글)
     */
    static isHangul(str){
        var pattern_kor = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
        if( pattern_kor.test(str) ) {
            return true
        } else {
            return false
        }
    }

    /**
     * like oracle's nvl()
     * @param {any} obj
     * @param {any} defaultValue
     * @returns {any}
     */
    static nvl(obj, defaultValue) {
        if (pp.isNotNull(obj)) {
            return obj;
        }
        //
        if (pp.isNull(defaultValue)) {
            return "";
        } else {
            return defaultValue;
        }
    }

    /**
     *
     * @param {string} url
     * @param {any} param case1~4
     * @param {Function} callbackSuccess
     * @param {any|undefined} option {'method':string, 'async':boolean, 'callbackError':function}
     */
    static submitAjax(url, param, callbackSuccess, option) {
        if (pp.isEmpty(url) || pp.isNull(param)) {
            return;
        }

        //
        let defaultSetting = {
            method: "POST",
            async: true,
            callbackError: null,
        };
        //
        let opt = pp.extend(defaultSetting, option);

        //
        let xhr = new XMLHttpRequest();
        //
        xhr.open(opt.method, url, opt.async);
        xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');

        //
        xhr.upload.onprogress = (e) => {
            if (!e.lengthComputable) {
                return;
            }

            //
            let percentComplete = (e.loaded / e.total) * 100;
            console.log(percentComplete + '% uploaded');
        }

        //
        xhr.onreadystatechange = function () {
            if (XMLHttpRequest.DONE === xhr.readyState) {
                let v = xhr.responseText.trim();

                //성공
                if (200 === xhr.status) {
                    //json 형식
                    if (v.startsWith("{")) {
                        callbackSuccess(JSON.parse(v));
                    } else {
                        //text 형식
                        callbackSuccess(v);
                    }
                } else {
                    //실패
                    if (pp.isNotNull(opt.callbackError)) {
                        opt.callbackError(v);
                    } else {
                        alert("오류가 발생했습니다.");
                    }
                }
            }
        };

        //
        let fd = new FormData();
        let p = pp.toKeyValue(param);
        //
        Object.keys(p).forEach(k=>{
            fd.append(k, p[k]);
        });

        //
        xhr.send(fd);
    }

    /**
     *
     * @param {string} url
     * @param {any} param case1~4
     * @returns {void}
     */
    static submitGet(url, param) {
        let htmlFormElement = ppui.createForm(param);

        //
        htmlFormElement.setAttribute("method", "get");
        //
        htmlFormElement.submit();
    }

    /**
     *
     * @param {string} url
     * @param {any} param case1~4
     * @returns {void}
     */
    static submitPost(url, param) {
        let htmlFormElement = ppui.createForm(param);

        //
        htmlFormElement.setAttribute("method", "post");
        //
        htmlFormElement.submit();
    }


    /**
     * 파일 업로드
     * @param {string} url
     * @param {File} file 
     * @param {Function} callbackSuccess
     * @param {any|undefined} option
     */
    static uploadFile(url, file, callbackSuccess, option){
        if(pp.isNull(file)){
            callbackSuccess({errorCode:'E_NULL'});
            return;
        }

        //파일 크기 검사
        if(!pp.checkFileSize(file, 123456)){
            callbackSuccess({errorCode:'E_FILE_SIZE'});
            return;
        }

        //파일 확장자 검사
        if(!pp.checkFileExt(file)){
            callbackSuccess({errorCode:'E_EXT'});
            return;
        }

        //
        let xhr = new XMLHttpRequest();
        xhr.open('post', url, true);
        xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');

        //
        xhr.onreadystatechange = ()=>{
            if(XMLHttpRequest.DONE === xhr.readyState){
                //성공
                if (200 === xhr.status) {
                    //json 형식
                    if (v.startsWith("{")) {
                        callbackSuccess(JSON.parse(v));
                    } else {
                        //text 형식
                        callbackSuccess(v);
                    }
                } else {
                    callbackSuccess({errorCode:'E_500'});
                    return;
                }
            }
        };

        //
        let fd = new FormData();
        fd.append('file', file);

        //
        xhr.send(fd);
    }

    /**
     * 업로드 가능한 확장자인지 검사
     * @param {File} file 
     * @param {Array<Exts>} arrOfExts
     * @returns {boolean}
     */
    static checkFileExt(file, arrOfExts){
        if(pp.isNull(file)){
            return false;
        }

        //TODO
        return true;
    }

    /**
     * 파일 크기 검사
     * @param {File} file 
     * @param {number} maxFileSize 
     * @returns {boolean}
     */
    static checkFileSize(file, maxFileSize){
        if(pp.isNull(file)){
            return false;
        }

        //
        return (file.size < maxFileSize);
    }

    /**
     * 파라미터 형 변환
     * @param {any} param 파라미터
     *  case1 {'name':string, 'value':any}
     *  case2 [case1]
     *  case3 {'key':'value'}
     *  case4 [case3]
     * @returns {any}
     */
    static toKeyValue(param) {
        let p = {};
        //case2, case4인 경우
        if (Array.isArray(param)) {
            return pp.toKeyValueFromArray(param);
        }

        //case1
        if (pp.isNotEmpty(param.name)) {
            return pp.toKeyValueFromNameValue(param.name, param.value);
        }

        //case3
        return param;
    }

    /**
     * toKeyValue()와 로직은 동일
     * 다른점. toKeyValue()는 Object 리턴, toMap()은 Map 리턴
     * case1~4의 정보는 @see toKeyValue()참고
     * @see https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Map
     * @param {Map|Array|object} param 
     * @returns {Map}
     */
    static toMap(param){

        /**
         * 
         * @param {Array<Map>|Array<Object>} arr 
         */
        let _toMapFromArray = function(arr){

            //
            if(0 == arr.length){
                return new Map();
            }

            //
            if(arr[0] instanceof Map){
                return _toMapFromMapArray(arr);
            }

            //
            if(arr[0] instanceof Object){
                return _toMapFromObjectArray(arr);
            }
        };

        /**
         * 
         * @param {Array<Object>} arr 
         */
        let _toMapFromObjectArray = function(arr){
            let resultMap = new Map();

            //
            arr.forEach((value,index)=>{
                let json = value;
                //
                if(!json.hasOwnProperty('name')){
                    return;
                }

                //
                resultMap = pp.extend(resultMap, json);
            });

            //
            return resultMap;
        };

        /**
         * 
         * @param {Array<Map>} arr 
         * @returns {Map}
         */
        let _toMapFromMapArray = function(arr){
            let resultMap = new Map();

            //
            arr.forEach((value,index)=>{
                let map = value;
                //
                if(!map.has('name')){
                    return;
                }

                //
                resultMap = pp.extend(resultMap, map);
            });

            //
            return resultMap;
        };



        //
        let map = new Map();

        //case2, case4
        if(Array.isArray(param)){
            return _toMapFromArray(param);
        }


        //case1
        if(param instanceof Object && param.hasOwnProperty('name')){
            let resultMap = new Map();
            //
            let k = param.name;
            let v = param.value;
            //
            resultMap.set(k, v);

            //
            return resultMap;
        }


        //
        return param;
    }

    /**
     * 파리미터 형 변환
     * @param {any} arr 파라미터 배열
     * @returns {any}
     */
    static toKeyValueFromArray(arr) {
        if (pp.isEmpty(arr)) {
            return {};
        }

        //
        let json = arr[0];

        //
        if (pp.isNotEmpty(json.name)) {
            //case2
            return pp.toKeyValueFromNameValueArray(arr);
        } else {
            //case4
            let p = {};
            //
            arr.forEach((json) => {
                p = pp.extend(p, json);
            });

            //
            return p;
        }
    }

    /**
     * 파라미터 형변환
     * @param {Array} arr 파라미터 배열. case2
     * @returns {any}
     */
    static toKeyValueFromNameValueArray(arr) {
        let p = {};

        //
        arr.forEach((json) => {
            p = pp.extend(p, this.toKeyValueFromNameValue(json.name, json.value));
        });

        //
        return p;
    }

    /**
     * 파라미터 형 변환
     * @param {string} name
     * @param {string} value
     * @returns {any}
     */
    static toKeyValueFromNameValue(name, value) {
        let k = name;
        let v = value;

        //
        let p = {};
        p[k] = v;
        //
        return p;
    }
}
