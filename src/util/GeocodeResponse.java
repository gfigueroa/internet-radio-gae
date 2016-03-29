package util;

import java.util.List;

public class GeocodeResponse {

    public enum Status {
        OK, ZERO_RESULTS, OVER_QUERY_LIMIT, REQUEST_DENIED, INVALID_REQUEST;
    }

    public static class Result {

        public static enum Type {
            street_address,
            route,
            intersection,
            political,
            country,
            administrative_area_level_1,
            administrative_area_level_2,
            administrative_area_level_3,
            colloquial_area,
            locality,
            sublocality,
            neighborhood,
            premise,
            subpremise,
            postal_code,
            natural_feature,
            airport,
            park,
            point_of_interest,
            post_box,
            street_number,
            floor,
            room;
        }

        public static class AddressComponent {

            private String long_name;
            private String short_name;
            private Type[] types;

            public String getLong_name() {
                return long_name;
            }

            public void setLong_name(String long_name) {
                this.long_name = long_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public Type[] getTypes() {
                return types;
            }

            public void setTypes(Type[] types) {
                this.types = types;
            }
        }

        private String formatted_address;
        private List<AddressComponent> address_components;
        private Geometry geometry;
        private Type[] types;

        public Type[] getTypes() {
            return types;
        }

        public void setTypes(Type[] types) {
            this.types = types;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public List<AddressComponent> getAddress_components() {
            return address_components;
        }

        public void setAddress_components(List<AddressComponent> address_components) {
            this.address_components = address_components;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

    }

    public static class Geometry {
        public static enum LocationType {
            ROOFTOP, RANGE_INTERPOLATED, GEOMETRIC_CENTER, APPROXIMATE;
        }

        public static class ViewPort {
            private Location northeast;
            private Location southwest;

            public Location getNortheast() {
                return northeast;
            }

            public void setNortheast(Location northeast) {
                this.northeast = northeast;
            }

            public Location getSouthwest() {
                return southwest;
            }

            public void setSouthwest(Location southwest) {
                this.southwest = southwest;
            }
        }

        private Location location;
        private LocationType location_type;
        private ViewPort viewport;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public LocationType getLocation_type() {
            return location_type;
        }

        public void setLocation_type(LocationType location_type) {
            this.location_type = location_type;
        }

        public ViewPort getViewport() {
            return viewport;
        }

        public void setViewport(ViewPort viewport) {
            this.viewport = viewport;
        }

    }

    private Status status;
    private List<Result> results;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}