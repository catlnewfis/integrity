var NameSpace = NameSpace || {};
NameSpace.TestSession = NameSpace.TestSession || {};
NameSpace.Attachment = NameSpace.Attachment || {};
NameSpace.History = NameSpace.History || {};
NameSpace.Relationship = NameSpace.Relationship || {};
NameSpace.TestResult = NameSpace.TestResult || {};
NameSpace.IntervalFor = NameSpace.IntervalFor || {};
NameSpace.SpendTime = NameSpace.SpendTime || {};
NameSpace.Store = NameSpace.Store || {};
NameSpace.Label = NameSpace.Label || {};
NameSpace.Branches = NameSpace.Branches || {};
NameSpace.Session = NameSpace.Session || {};
NameSpace.Constraints = NameSpace.Constraints || {};
NameSpace.ScrennWidth = NameSpace.ScrennWidth ||{};

Array.prototype.indexOf = function(val) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == val) {
			return i;
		}
	}
	return -1;
};

Array.prototype.remove = function(val) {
	var index = this.indexOf(val);
		if (index > -1) {
			return this.splice(index, 1);
		}
};

jQuery.ajaxSetup({ timeout:0 });

$.jqAjax = $.ajax;
$.ajax = function(obj){
	obj.complete = function(xhr){
		if (xhr.getResponseHeader("sessionstatus")) {
			window.location.href="/integrity/login.jsp";
		}
	};
	$.jqAjax(obj);
};

NameSpace.parseDate = function(date) {
	if(date) {
		if(date.time) {
			return new Date(date.time);
		}
		return new Date(date);
	}
}
