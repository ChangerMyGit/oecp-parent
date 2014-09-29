/*!
 * Extensible 1.0.2
 * Copyright(c) 2010-2012 Extensible, LLC
 * licensing@ext.ensible.com
 * http://ext.ensible.com
 */
var today = new Date().clearTime();
Ext.ensible.sample.EventData = {
    "evts":[{
        "id":1001,
        "title":"Vacation123",
        "start":'2014-03-09 10:10:00',
        "end":'2014-03-19 13:10:00',
		"notes":"Have fun"
    },{
        "id":1002,
        "title":"Lunch with Matt",
        "start":today.add(Date.HOUR, 11).add(Date.MINUTE, 30),
        "end":today.add(Date.HOUR, 13),
        "loc":"Chuy's!",
        "url":"http://chuys.com",
		"notes":"Order the queso",
        "rem":"15"
    },{
        "id":1003,
        "title":"Project due",
        "start":today.add(Date.HOUR, 15),
        "end":today.add(Date.HOUR, 15)
    },{
        "id":1004,
        "title":"Sarah's birthday",
        "start":today,
        "end":today,
        "notes":"Need to get a gift",
        "ad":true
    },{
        "id":1005,
        "title":"A long one...",
        "start":today.add(Date.DAY, -12),
        "end":today.add(Date.DAY, 10).add(Date.SECOND, -1),
        "ad":true
    },{
        "id":1006,
        "title":"School holiday",
        "start":today.add(Date.DAY, 5),
        "end":today.add(Date.DAY, 7).add(Date.SECOND, -1),
        "ad":true,
        "rem":"2880"
    },{
        "id":1007,
        "title":"Haircut",
        "start":today.add(Date.HOUR, 9),
        "end":today.add(Date.HOUR, 9).add(Date.MINUTE, 30),
		"notes":"Get cash on the way"
    },{
        "id":1008,
        "title":"An old event",
        "start":today.add(Date.DAY, -30),
        "end":today.add(Date.DAY, -28),
        "ad":true
    },{
        "id":1009,
        "title":"Board meeting",
        "start":today.add(Date.DAY, -2).add(Date.HOUR, 13),
        "end":today.add(Date.DAY, -2).add(Date.HOUR, 18),
        "loc":"ABC Inc.",
        "rem":"60"
    },{
        "id":1010,
        "title":"Jenny's final exams",
        "start":today.add(Date.DAY, -2),
        "end":today.add(Date.DAY, 3).add(Date.SECOND, -1),
        "ad":true
    },{
        "id":1011,
        "title":"Movie night",
        "start":today.add(Date.DAY, 2).add(Date.HOUR, 19),
        "end":today.add(Date.DAY, 2).add(Date.HOUR, 23),
        "notes":"Don't forget the tickets!",
        "rem":"60"
    },{
        "id":1012,
        "title":"Gina's basketball tournament",
        "start":today.add(Date.DAY, 8).add(Date.HOUR, 8),
        "end":today.add(Date.DAY, 10).add(Date.HOUR, 17)
    },{
        "id":1013,
        "title":"Toby's soccer game",
        "start":today.add(Date.DAY, 5).add(Date.HOUR, 10),
        "end":today.add(Date.DAY, 5).add(Date.HOUR, 12)
    }]
};
