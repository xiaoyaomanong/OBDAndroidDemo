package com.example.obdandroid.ui.entity;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/9/8 0008
 * 描述：
 */
public class ReverseGeoAddressEntity {
    /*
     *{"status":0,"result":{"location":{"lng":103.84665699999995,"lat":36.05027689145033},"formatted_address":"甘肃省兰州市城关区皋兰路9号","business":"民主西路,金昌南路,皋兰路","addressComponent":{"country":"中国","country_code":0,"country_code_iso":"CHN","country_code_iso2":"CN","province":"甘肃省","city":"兰州市","city_level":2,"district":"城关区","town":"","town_code":"","adcode":"620102","street":"皋兰路","street_number":"9号","direction":"附近","distance":"4"},"pois":[{"addr":"甘肃省兰州市城关区皋兰路7号(近民主西路)","cp":"","direction":"内","distance":"0","name":"虹云宾馆(皋兰路)","poiType":"酒店","point":{"x":103.84655134156366,"y":36.05030497802438},"tag":"酒店;三星级","tel":"","uid":"a5ef680f3f07a8ceb8df8ff4","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"兰州市城关区皋兰路1号","cp":" ","direction":"东北","distance":"88","name":"工贸大厦","poiType":"房地产","point":{"x":103.84627286685556,"y":36.04971406792947},"tag":"房地产;写字楼","tel":"","uid":"432955fba209f60184330480","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"兰州市城关区皋兰路4号","cp":" ","direction":"西","distance":"75","name":"宏宇大厦","poiType":"房地产","point":{"x":103.84733286735734,"y":36.050210140903228},"tag":"房地产;写字楼","tel":"","uid":"e1bb9e82ace70c5ac0364492","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"兰州市城关区皋兰路虹云宾馆(皋兰路)","cp":" ","direction":"东北","distance":"60","name":"凯兴国际","poiType":"房地产","point":{"x":103.84629083296577,"y":36.04994751442766},"tag":"房地产;住宅区","tel":"","uid":"cef0d0972a7b6e0803ff2d06","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"皋兰路44号宏宇大厦f座","cp":" ","direction":"西","distance":"75","name":"宏宇大厦-f座","poiType":"房地产","point":{"x":103.84733286735734,"y":36.050210140903228},"tag":"房地产;写字楼","tel":"","uid":"06cb8ed49547a5825cbe8685","zip":"","parent_poi":{"name":"宏宇大厦","tag":"房地产;写字楼","addr":"兰州市城关区皋兰路4号","point":{"x":103.84733286735734,"y":36.050210140903228},"direction":"西","distance":"75","uid":"e1bb9e82ace70c5ac0364492"}},{"addr":"甘肃省兰州市城关区皋兰路29号","cp":" ","direction":"南","distance":"70","name":"扶贫大厦","poiType":"房地产","point":{"x":103.84672201961056,"y":36.05078645701511},"tag":"房地产;写字楼","tel":"","uid":"715415993d202f6fa80dd82c","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"皋兰路33号","cp":" ","direction":"南","distance":"84","name":"建银大厦","poiType":"房地产","point":{"x":103.84675795183095,"y":36.050888588540249},"tag":"房地产;写字楼","tel":"","uid":"7d57cfc8a515efdd9c09f7ab","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"民主西路6号","cp":" ","direction":"东北","distance":"142","name":"光源大厦","poiType":"房地产","point":{"x":103.84561710383328,"y":36.049670296633298},"tag":"房地产;写字楼","tel":"","uid":"0adce3e24df5c96180a00c78","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"区皋兰路1号(铁路局工贸大厦院内)","cp":" ","direction":"东","distance":"104","name":"盛贸华府","poiType":"房地产","point":{"x":103.84573388354957,"y":36.050428995624269},"tag":"房地产;住宅区","tel":"","uid":"1d662f3fb54e1d8709a8d4d8","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"民主东路300号","cp":" ","direction":"西北","distance":"114","name":"锦华写字楼","poiType":"房地产","point":{"x":103.84708134181455,"y":36.04951709690334},"tag":"房地产;写字楼","tel":"","uid":"a336de5fb5b5576b6da3cc5f","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"兰州城关区皋兰路20号","cp":" ","direction":"西南","distance":"172","name":"兴中大厦","poiType":"房地产","point":{"x":103.84749456234913,"y":36.05133358862523},"tag":"房地产;写字楼","tel":"","uid":"0a15c7ec4d4366fced74f878","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"民主东路385号","cp":" ","direction":"北","distance":"220","name":"金轮大厦","poiType":"房地产","point":{"x":103.84672201961056,"y":36.04867084535609},"tag":"房地产;写字楼","tel":"","uid":"803708f158f74f0400897299","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"兰州市城关区民主东路296号","cp":" ","direction":"西北","distance":"221","name":"和颐至尚酒店(东方红广场店)","poiType":"酒店","point":{"x":103.84826710508772,"y":36.049327420630458},"tag":"酒店;其他","tel":"","uid":"7777610d9ca2f2d5c5effbde","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"皋兰路67号","cp":" ","direction":"南","distance":"184","name":"工商大楼","poiType":"房地产","point":{"x":103.84700049431865,"y":36.051596210435267},"tag":"房地产;写字楼","tel":"","uid":"24617059674926fce115f9dc","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"民主东路365号","cp":" ","direction":"西北","distance":"275","name":"中铁科技大厦","poiType":"房地产","point":{"x":103.84808744398572,"y":36.048634368789979},"tag":"房地产;写字楼","tel":"","uid":"fcee2258aba97b68873304ad","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"甘肃省兰州市城关区民主东路365号","cp":" ","direction":"西北","distance":"339","name":"艺海大酒店","poiType":"酒店","point":{"x":103.8488150714488,"y":36.04852493898929},"tag":"酒店;其他","tel":"","uid":"aa0aae368c22d7a3cb213d9a","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"铁路西村街道民主西路3号7天酒店旁逸品华府","cp":" ","direction":"东北","distance":"255","name":"逸品华府","poiType":"房地产","point":{"x":103.8447637135988,"y":36.049217991801729},"tag":"房地产;住宅区","tel":"","uid":"5f320cdc65cd6f4d2806ce68","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"民主东路365号","cp":" ","direction":"西北","distance":"281","name":"中铁科技大厦-A塔","poiType":"房地产","point":{"x":103.84808744398572,"y":36.04858330156875},"tag":"房地产;写字楼","tel":"","uid":"7522d19f678890f5ab059d63","zip":"","parent_poi":{"name":"中铁科技大厦","tag":"房地产;写字楼","addr":"民主东路365号","point":{"x":103.84808744398572,"y":36.048634368789979},"direction":"西北","distance":"275","uid":"fcee2258aba97b68873304ad"}},{"addr":"兰州市城关区民主西路3号","cp":" ","direction":"东北","distance":"291","name":"商贸写字楼","poiType":"房地产","point":{"x":103.84432354389891,"y":36.04930553487697},"tag":"房地产;写字楼","tel":"","uid":"55fa2c0b5c9994341ce9995c","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}},{"addr":"兰州市城关区麦积山路周家庄耿家庄社区居委会南侧","cp":" ","direction":"西南","distance":"300","name":"周家庄小区","poiType":"房地产","point":{"x":103.84836591869382,"y":36.05197555148911},"tag":"房地产;住宅区","tel":"","uid":"8d4adbc9e2c7848445572643","zip":"","parent_poi":{"name":"","tag":"","addr":"","point":{"x":0.0,"y":0.0},"direction":"","distance":"","uid":""}}],"roads":[],"poiRegions":[{"direction_desc":"内","name":"虹云宾馆(皋兰路)","tag":"酒店;三星级","uid":"a5ef680f3f07a8ceb8df8ff4","distance":"0"}],"sematic_description":"虹云宾馆(皋兰路)内,凯兴国际东北60米","cityCode":36}}
     * */

    private int status;
    private ResultEntity result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public static class ResultEntity {
        private LocationEntity location;
        private String formatted_address;
        private String business;
        private AddressComponentEntity addressComponent;
        private List<PoisEntity> pois;
        private List<?> roads;
        private List<PoiRegionsEntity> poiRegions;
        private String sematic_description;
        private int cityCode;

        public LocationEntity getLocation() {
            return location;
        }

        public void setLocation(LocationEntity location) {
            this.location = location;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public AddressComponentEntity getAddressComponent() {
            return addressComponent;
        }

        public void setAddressComponent(AddressComponentEntity addressComponent) {
            this.addressComponent = addressComponent;
        }

        public List<PoisEntity> getPois() {
            return pois;
        }

        public void setPois(List<PoisEntity> pois) {
            this.pois = pois;
        }

        public List<?> getRoads() {
            return roads;
        }

        public void setRoads(List<?> roads) {
            this.roads = roads;
        }

        public List<PoiRegionsEntity> getPoiRegions() {
            return poiRegions;
        }

        public void setPoiRegions(List<PoiRegionsEntity> poiRegions) {
            this.poiRegions = poiRegions;
        }

        public String getSematic_description() {
            return sematic_description;
        }

        public void setSematic_description(String sematic_description) {
            this.sematic_description = sematic_description;
        }

        public int getCityCode() {
            return cityCode;
        }

        public void setCityCode(int cityCode) {
            this.cityCode = cityCode;
        }

        public static class LocationEntity {
            private double lng;
            private double lat;

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }
        }

        public static class AddressComponentEntity {
            private String country;
            private int country_code;
            private String country_code_iso;
            private String country_code_iso2;
            private String province;
            private String city;
            private int city_level;
            private String district;
            private String town;
            private String town_code;
            private String adcode;
            private String street;
            private String street_number;
            private String direction;
            private String distance;

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public int getCountry_code() {
                return country_code;
            }

            public void setCountry_code(int country_code) {
                this.country_code = country_code;
            }

            public String getCountry_code_iso() {
                return country_code_iso;
            }

            public void setCountry_code_iso(String country_code_iso) {
                this.country_code_iso = country_code_iso;
            }

            public String getCountry_code_iso2() {
                return country_code_iso2;
            }

            public void setCountry_code_iso2(String country_code_iso2) {
                this.country_code_iso2 = country_code_iso2;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public int getCity_level() {
                return city_level;
            }

            public void setCity_level(int city_level) {
                this.city_level = city_level;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getTown() {
                return town;
            }

            public void setTown(String town) {
                this.town = town;
            }

            public String getTown_code() {
                return town_code;
            }

            public void setTown_code(String town_code) {
                this.town_code = town_code;
            }

            public String getAdcode() {
                return adcode;
            }

            public void setAdcode(String adcode) {
                this.adcode = adcode;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getStreet_number() {
                return street_number;
            }

            public void setStreet_number(String street_number) {
                this.street_number = street_number;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }
        }

        public static class PoisEntity {
            private String addr;
            private String cp;
            private String direction;
            private String distance;
            private String name;
            private String poiType;
            private PointEntity point;
            private String tag;
            private String tel;
            private String uid;
            private String zip;
            private ParentPoiEntity parent_poi;

            public String getAddr() {
                return addr;
            }

            public void setAddr(String addr) {
                this.addr = addr;
            }

            public String getCp() {
                return cp;
            }

            public void setCp(String cp) {
                this.cp = cp;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPoiType() {
                return poiType;
            }

            public void setPoiType(String poiType) {
                this.poiType = poiType;
            }

            public PointEntity getPoint() {
                return point;
            }

            public void setPoint(PointEntity point) {
                this.point = point;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getZip() {
                return zip;
            }

            public void setZip(String zip) {
                this.zip = zip;
            }

            public ParentPoiEntity getParent_poi() {
                return parent_poi;
            }

            public void setParent_poi(ParentPoiEntity parent_poi) {
                this.parent_poi = parent_poi;
            }

            public static class PointEntity {
                private double x;
                private double y;

                public double getX() {
                    return x;
                }

                public void setX(double x) {
                    this.x = x;
                }

                public double getY() {
                    return y;
                }

                public void setY(double y) {
                    this.y = y;
                }
            }

            public static class ParentPoiEntity {
                private String name;
                private String tag;
                private String addr;
                private PointEntity point;
                private String direction;
                private String distance;
                private String uid;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public String getAddr() {
                    return addr;
                }

                public void setAddr(String addr) {
                    this.addr = addr;
                }

                public PointEntity getPoint() {
                    return point;
                }

                public void setPoint(PointEntity point) {
                    this.point = point;
                }

                public String getDirection() {
                    return direction;
                }

                public void setDirection(String direction) {
                    this.direction = direction;
                }

                public String getDistance() {
                    return distance;
                }

                public void setDistance(String distance) {
                    this.distance = distance;
                }

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public static class PointEntity {
                    private int x;
                    private int y;

                    public int getX() {
                        return x;
                    }

                    public void setX(int x) {
                        this.x = x;
                    }

                    public int getY() {
                        return y;
                    }

                    public void setY(int y) {
                        this.y = y;
                    }
                }
            }
        }

        public static class PoiRegionsEntity {
            private String direction_desc;
            private String name;
            private String tag;
            private String uid;
            private String distance;

            public String getDirection_desc() {
                return direction_desc;
            }

            public void setDirection_desc(String direction_desc) {
                this.direction_desc = direction_desc;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }
        }
    }
}