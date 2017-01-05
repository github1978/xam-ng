$(document).ready(function(){
    $.fn.api.settings.api = {
      'get slaves' : '/xamng/case/getSlaves',
    };

    var dropdown = $('.ui.fluid.multiple.search.normal.selection.dropdown.slave')
    .dropdown({
        useLabels: true,
        allowAdditions: true,
        fields:{
            name:'host',
            value:'id',
            text:'host'
        },
        apiSettings:{
            action:'get slaves'
        },
    });
});