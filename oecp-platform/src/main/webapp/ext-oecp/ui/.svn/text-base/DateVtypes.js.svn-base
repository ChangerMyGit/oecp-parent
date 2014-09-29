/**
 * 程正良
 * 日期校验引用：
 * 有效结束日期不能大于有效开始日期
 * 使用举例：
 * {fieldLabel: '有效开始日期',id:'startdt',endDateField: 'enddt', vtype: 'daterange'}
 * {fieldLabel: '有效开始日期',id:'enddt',endDateField: 'startdt', vtype: 'daterange'}
 */
Ext.apply(Ext.form.VTypes, {
    daterange : function(val, field) {
        var date = field.parseDate(val);

        if(!date){
            return false;
        }
        if (field.startDateField) {
            var start = Ext.getCmp(field.startDateField);
            if (!start.maxValue || (date.getTime() != start.maxValue.getTime())) {
                start.setMaxValue(date);
                start.validate();
            }
        }
        else if (field.endDateField) {
            var end = Ext.getCmp(field.endDateField);
            if (!end.minValue || (date.getTime() != end.minValue.getTime())) {
                end.setMinValue(date);
                end.validate();
            }
        }
        /*
         * Always return true since we're only using this vtype to set the
         * min/max allowed values (these are tested for after the vtype test)
         */
        return true;
    }});