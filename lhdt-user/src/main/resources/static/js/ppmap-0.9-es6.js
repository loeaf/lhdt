/**
 * cesium관련 이것저것
 * @author gravity
 * @since
 *	20200804	init
 */
class PpMap{
	constructor(){
		this._viewer = null;
	}
	
	/**
	 *
	 */
	get viewer(){
		return this._viewer;
	}
	
	/**
	 *
	 */
	set viewer(value){
		this._viewer = value; 
	}
	
	
	/**
	 * @param {Cartesian3|LonLatAlt} ctsnOrXyz
	 * @param {Cartesian3} direction
	 */
	addDirectionRay(ctsnOrXyz, direction){
		const origin = this.toCartesian3(ctsnOrXyz);
		
		const directionRay = Cesium.Cartesian3.multiplyByScalar(direction, 100000, new Cesium.Cartesian3());
		Cesium.Cartesian3.add(origin, directionRay, directionRay);
		
		this._viewer.entities.add({
		    polyline: {
		        positions: [origin, directionRay],
		        width: 5,
		        material: Cesium.Color.WHITE
		    }
		});
	}
	
	
	
	/**
	 * @param {Cartesian3|LonLatAlt} ctsn1
	 * @param {Cartesian3|LonLatAlt} ctsn2
	 * @returns {Cartesian3} 
	 */
	getDirection(ctsn1, ctsn2){
		//
		const origin = this.toCartesian3(ctsn1);
		const target = this.toCartesian3(ctsn2);
		
		//
		const direction = Cesium.Cartesian3.subtract(target, origin, new Cesium.Cartesian3());
		//
		Cesium.Cartesian3.normalize(direction, direction);

		//
		return direction;
	}
	
	
	
	
	/**
	* header값 계산 & 리턴
	* @see https://stackoverflow.com/questions/58323971/cesium-calculate-heading-and-pitch-from-2-cartesian3-points
	* @param {Cartesian3|LonLatAlt} pointA  
	* @param {Cartesian3|LonLatAlt} pointB  
	* @return {Number}
	*/
	getHeading(pointA, pointB){
		//
		const ctsnA = this.toCartesian3(pointA);
		const ctsnB = this.toCartesian3(pointB);

		//		
		const transform=Cesium.Transforms.eastNorthUpToFixedFrame(ctsnA);
	    const positionvector=Cesium.Cartesian3.subtract(ctsnB,ctsnA,new Cesium.Cartesian3());
	    const vector=Cesium.Matrix4.multiplyByPointAsVector(Cesium.Matrix4.inverse(transform,new Cesium.Matrix4()),positionvector,new Cesium.Cartesian3());
	    const direction=Cesium.Cartesian3.normalize(vector,new Cesium.Cartesian3());
	    //heading
	    const heading=Math.atan2(direction.y,direction.x)-Cesium.Math.PI_OVER_TWO;
	    //pitch
	    const pitch=Cesium.Math.PI_OVER_TWO-Cesium.Math.acosClamped(direction.z);
	    
	    //
	    return Cesium.Math.toDegrees(Cesium.Math.TWO_PI-Cesium.Math.zeroToTwoPi(heading));
	}
	
	
	
	/**
	 * @param {Cartesian3|lonLatAlt} ctsnOrXyz
	 * @param {HeadingPitchRoll} hpr
	 */
	flyTo(ctsnOrXyz, hpr){
		const ctsn = this.toCartesian3(ctsnOrXyz);
		
		//
		this._viewer.scene.camera.flyTo({
			destination: ctsn,
			orientation:{
				heading: hpr.heading,
				pitch: hpr.pitch,
				roll: hpr.roll
			}
		});		
	}
	

	
	
	/**
	 * degree object인지 여부
	 * @param {any} obj
     * @returns {boolean}
	 */
	isDegree(obj){
		if('object' !== typeof(obj)){
			return false;
		}
		
		//
		return (undefined !== obj['lon'] ? true : false);
	}
	
	
	/**
	 * 파라미터를 Cartesian3로 변환
     * @param {any} 가변적 Cartesian3 or LonLatAlt or lon,lat,alt
     * @returns {Cartesian3}
	 */
	toCartesian3(){
		const args = arguments;
		
		
		if(0 === args.length){
			throw Error('');
		}
		
		//
		if(1 === args.length && args[0] instanceof(Cesium.Cartesian3)){
			return args[0];
		}
		
		//
		if(1 === args.length && this.isDegree(args[0])){
			return Cesium.Cartesian3.fromDegrees(args[0].lon, args[0].lat, args[0].alt||0.0);
		}
		
		//
		const lon = args[0];
		const lat = args[1];
		const alt = args[2]||0.0;
		
		//
		return Cesium.Cartesian3.fromDegrees(lon, lat, alt);
	}
	
	
}

//
const ppmap = new PpMap();