Mago3D.ManagerUtils.geographicToWkt = function(geographic, type) {
	var wkt = '';
	
	switch(type) {
		case 'POINT' : {
			wkt = 'POINT (';
			wkt += geographic.longitude;
			wkt += ' ';
			wkt += geographic.latitude;
			wkt += ')';
			break;
		}
		case 'LINE' : {
			wkt = 'LINESTRING (';
			for(var i=0,len=geographic.length;i<len;i++) {
				if(i>0) {
					wkt += ',';
				}
				wkt += geographic[i].longitude;
				wkt += ' ';
				wkt += geographic[i].latitude;
			}
			wkt += ')';
			break;
		}
		case 'POLYGON' : {
			wkt = 'POLYGON ((';
			for(var i=0,len=geographic.length;i<len;i++) {
				if(i>0) {
					wkt += ',';
				}
				wkt += geographic[i].longitude;
				wkt += ' ';
				wkt += geographic[i].latitude;
			}
			wkt += ',';
			wkt += geographic[0].longitude;
			wkt += ' ';
			wkt += geographic[0].latitude;
			wkt += '))';
			break;
		}
	}
	
	function coordToString(coord,str) {
		var text = str ? str : '';
		if(Array.isArray(coord)) {
			for(var i=0,len=coord.length;i<len;i++) {
				coordToString(coord[i],text);
			}
		} else {
			if(text) {
				text += ',';
			}
			text += coord.longitude;
			text += ' ';
			text += coord.latitude;
		}
		
		return text;
	}
	
	return wkt;
}

Mago3D.ManagerUtils.getCoordinateFromWKT = function(wkt, type) {
	switch(type) {
		case 'POINT' : {
			var removePrefix = wkt.replace(/\bpoint\b\s*\(/i, "");
			var removeSuffix = removePrefix.replace(/\s*\)\s*$/, "");
			var coordinates = removeSuffix.match(/[+-]?\d*(\.?\d+)/g);
			return coordinates;
		}
	}
}
Mago3D.Color.prototype.getHexCode = function() {
	var r = this.r * 255;
	var g = this.g * 255;
	var b = this.b * 255;
	
	var hexR = r.toString(16).padStart(2, '0'); //String.padStart i.e no support..TT 
	var hexG = g.toString(16).padStart(2, '0');
	var hexB = b.toString(16).padStart(2, '0');
	
	return '#'+hexR+hexG+hexB;
}
Mago3D.Node.prototype.getCurrentGeoLocationData = function() {
	if(!this.isReadyToRender() || !this.data || !this.data.geoLocDataManager) {
		throw new Error('this node is not ready to use.');
	}
	
	return this.data.geoLocDataManager.getCurrentGeoLocationData()
}

Mago3D.MagoManager.prototype.addCluster = function(cluster) {
	if(!cluster || !cluster instanceof Mago3D.Cluster) {
		throw new Error('cluster is required.');
	}
	
	this.cluster = cluster;
}

Mago3D.MagoManager.prototype.clearCluster = function() {
	this.objMarkerManager.setMarkerByCondition(function(om){
		return !om.tree;
	});
	this.cluster = undefined;
}

Mago3D.tempCredit = function(viewer) {
	var creditDisplay = viewer.scene.frameState.creditDisplay;
	var mago3d_credit = new Cesium.Credit('<a href="http://www.mago3d.com/" target="_blank"><img class="mago3d_logo" src="/images/logo_mago3d.png" title="Mago3D" alt="Mago3D" /></a>', true);
	creditDisplay.addDefaultCredit(mago3d_credit);
}

/**
 * 주어진 3차원 점을 포함하는 영역으로 이동
 * 
 * @param {Point3D} point 3차원 점
 */
Mago3D.MagoManager.prototype.flyToBox = function(pointsArray) {
	var bbox = new Mago3D.BoundingBox();
	bbox.init(pointsArray[0]);
	bbox.addPoint(pointsArray[1]);

	this.boundingSphere_Aux = new Mago3D.Sphere();
	this.boundingSphere_Aux.radius = bbox.getRadiusAprox();
	
	if (this.isCesiumGlobe())
	{
		var bboxCenterPoint = bbox.getCenterPoint();
		this.boundingSphere_Aux.center = Cesium.Cartesian3.clone({x:bboxCenterPoint.x,y:bboxCenterPoint.y,z:bboxCenterPoint.z});
		var seconds = 3;
		this.scene.camera.flyToBoundingSphere(this.boundingSphere_Aux, {duration:seconds});
	}
	
	
}