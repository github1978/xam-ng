$(document).ready(function(){

    var dropdowns = [
        {id:'#slave',option:{
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
        }},
        {id:'#browser',option:{
            useLabels: true,
            allowAdditions: true
        }},
        {id:'operates',option:''
        },
    ];

    $.fn.api.settings.api = {
      'get slaves' : '/xamng/case/getSlaves',
    };

    $.each(dropdowns,function(i,v){
      if(v.option==''){
        $(v.id).dropdown();
      }else{
        $(v.id).dropdown(v.option);
      }
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