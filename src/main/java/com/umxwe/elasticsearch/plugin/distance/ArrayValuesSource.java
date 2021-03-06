package com.umxwe.elasticsearch.plugin.distance;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.SortedNumericDocValues;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.fielddata.MultiGeoPointValues;
import org.elasticsearch.index.fielddata.NumericDoubleValues;
import org.elasticsearch.index.fielddata.SortedBinaryDocValues;
import org.elasticsearch.index.fielddata.SortedNumericDoubleValues;
import org.elasticsearch.search.MultiValueMode;
import org.elasticsearch.search.aggregations.support.ValuesSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName MlutArrayValuesSource
 * @Description Todo
 * @Author owen(umxwe)
 * @Date 2021/2/24
 */
public class ArrayValuesSource<VS extends ValuesSource,VD extends ValuesSource> {
    private final static Logger logger = LoggerFactory.getLogger(ArrayValuesSource.class);

    protected MultiValueMode multiValueMode;
    protected String[] names;
    protected VS  values1;
    protected VD  values2;

    public static class MultArrayValuesSource extends ArrayValuesSource<ValuesSource.Numeric,ValuesSource.GeoPoint> {

        public MultArrayValuesSource(Map<String, ValuesSource> valuesSources, MultiValueMode multiValueMode) {
            super(valuesSources, multiValueMode);
            if (valuesSources != null) {
                for (Map.Entry<String, ValuesSource> item:valuesSources.entrySet()
                     ) {

                    if(item.getValue() instanceof ValuesSource.Numeric == true){
                        this.setValues1((ValuesSource.Numeric) item.getValue());
                    }
                    else if(item.getValue() instanceof ValuesSource.GeoPoint == true){
                        this.setValues2((ValuesSource.GeoPoint) item.getValue());
                    }else {
                        logger.info("只支持数字类型和Geo_Point类型");
                    }
                }

            } else {
                this.values1 = ValuesSource.Numeric.EMPTY;
                this.values2 = ValuesSource.GeoPoint.EMPTY;
            }
        }
//
//        public Tuple<NumericDoubleValues, MultiGeoPointValues> getField(final int ordinal, LeafReaderContext ctx) throws IOException {
//            if (ordinal > names.length) {
//                throw new IndexOutOfBoundsException("ValuesSource array index " + ordinal + " out of bounds");
//            }
//            return new Tuple<>(multiValueMode.select(values1[ordinal].doubleValues(ctx)),values2[ordinal].geoPointValues(ctx));
//        }
    }
    private ArrayValuesSource(Map<String, ?> valuesSources, MultiValueMode multiValueMode) {
        if (valuesSources != null) {
            this.names = valuesSources.keySet().toArray(new String[0]);
        }
        this.multiValueMode = multiValueMode;
    }


    public boolean needsScores() {
        return false;
    }

    public String[] fieldNames() {
        return this.names;
    }

    public VS getValues1() {
        return values1;
    }

    public VD getValues2() {
        return values2;
    }

    public void setValues1(VS values1) {
        this.values1 = values1;
    }

    public void setValues2(VD values2) {
        this.values2 = values2;
    }
}
