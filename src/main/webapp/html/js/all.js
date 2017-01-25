$(document).ready(function(){
    $.fn.api.settings.api = {
      'get slaves' : '/xamng/case/getSlaves',
    };

    $('#slave').dropdown({
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

    $('#browser').dropdown({
        useLabels: true,
        allowAdditions: true
    });

    $('.excute').click(function(e){
        $(this).addClass('loading');
    }).api({
        url:'/xamng/case/excute',
        method:'post',
        beforeSend:function(settings){
            settings.data.slave = $('#slave').dropdown('get value');
            settings.data.browser =  $('#browser').dropdown('get value');
            return settings;
        }
    })
    ;

    $('.menu .item')
      .tab()
    ;

    $('.uiCaseStep')
      .accordion({
        selector: {
          trigger: '.stepName'
        }
      })
    ;
});