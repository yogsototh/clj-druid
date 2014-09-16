(ns clj-druid.schemas
  (:require [schema.core :as s]))

(s/defschema granularity
  "Druid Granularity option schema"
  (s/enum :all
          :none
          :minute
          :fifteen_minute
          :thirty_minute
          :hour
          :day))

(s/defschema limitSpec
  "Druid limitSpec option schema"
  {:type (s/enum :default) :limit Long :columns [String]})


(s/defschema filterSchema
  "Druid filter field option schema"
  {:type (s/enum :selector :regex :and :or :not)
   (s/optional-key :dimension) String
   (s/optional-key :value) String
   (s/optional-key :pattern) String
   (s/optional-key :fields) [(s/recursive #'filterSchema)]
   (s/optional-key :field) (s/recursive #'filterSchema)})

(s/defschema aggregationSchema
  "Druid filter field option schema"
  {:type (s/enum :count :longSum :doubleSum :min :max)
   :name String
   (s/optional-key :fieldName) String})

(s/defschema postAggregationSchema
  "Druid filter field option schema"
  {:type (s/enum :arithmetic :fieldAccess :constant :javascript)
   :name String
   (s/optional-key :fields) [(s/recursive #'postAggregationSchema)]
   (s/optional-key :fieldName) String
   (s/optional-key :value) String
   (s/optional-key :fn) String
   (s/optional-key :function) String})

(s/defschema intervalSchema
  "Druid interval schema"
  [String])

(s/defschema havingSchema
  "Druid having option schema"
  {:type (s/enum :equalTo
                 :greaterThan
                 :lessThan
                 :and
                 :or
                 :not)

   (s/optional-key :aggregation) String
   (s/optional-key :value) (s/either String Long)
   (s/optional-key :havingSpecs) [(s/recursive #'havingSchema)]})


(s/defschema QueryType
  "Druid queryType option schema"
  (s/enum :groupBy
          :search
          :segmentMetadata
          :timeBoundary
          :timeseries
          :topN))


(s/defschema SearchQuery
  "Druid queryType option query schema"
  {:type (s/enum :insensitive_contains :fragment)
   (s/optional-key :value) (s/either String Long)
   (s/optional-key :values) [(s/either String Long)]})

(s/defschema SearchSort
  "Druid queryType search sort schema"
  {:type (s/enum :lexicographic :strlen)})


(s/defschema SegmentMetadataToInclude
  "Druid SegmentMetadata toInclude option schema"
  {:type (s/enum :all :none :list)
   :columns [String]})


(s/defschema groupBy
  "GroupBy query schema"
  {:queryType (s/enum :groupBy)
   :dataSource String
   :dimensions [String]
   :granularity granularity
   :aggregations [aggregationSchema]
   :intervals intervalSchema
   (s/optional-key :postAggregations) [postAggregationSchema]
   (s/optional-key :limitSpec) limitSpec
   (s/optional-key :having) havingSchema
   (s/optional-key :filter) filterSchema})


(s/defschema search
  "Search query schema"
  {:queryType (s/enum :search)
   :dataSource String
   :query SearchQuery
   :sort SearchSort
   :granularity granularity
   :intervals intervalSchema
   (s/optional-key :searchDimensions) [String]
   (s/optional-key :filter) filterSchema})


(s/defschema segmentMetadata
  "Segment Metadata query schema"
  {:queryType (s/enum :segmentMetadata)
   :dataSource String
   :intervals intervalSchema
   (s/optional-key :toInclude) SegmentMetadataToInclude
   (s/optional-key :merge) Boolean})


(s/defschema topNMetricSpec
  "topN metric option schema"
  (s/either {:type (s/enum :numeric :lexicographic :alphaNumeric :inverted)
             (s/optional-key :metric) String
             (s/optional-key :previousStop) String} String))

(s/defschema timeBoundary
  "Segment Metadata query schema"
  {:queryType (s/enum :timeBoundary)
   :dataSource String
   (s/optional-key :toInclude) SegmentMetadataToInclude
   (s/optional-key :bound) (s/enum :minTime :maxTime)})


(s/defschema timeseries
  "timeseries query schema"
  {:queryType (s/enum :timeseries)
   :dataSource String
   :granularity granularity
   :aggregations [aggregationSchema]
   :intervals intervalSchema
   (s/optional-key :postAggregations) [postAggregationSchema]
   (s/optional-key :filter) filterSchema})


(s/defschema topN
  "topN query schema"
  {:queryType (s/enum :topN)
   :dataSource String
   :granularity granularity
   :dimension String
   :metric topNMetricSpec
   :threshold Long
   :aggregations [aggregationSchema]
   :intervals intervalSchema
   (s/optional-key :postAggregations) [postAggregationSchema]
   (s/optional-key :filter) filterSchema})