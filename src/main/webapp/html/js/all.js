$(document).ready(function(){

    $.fn.api.settings.api = {
      'get slaves' : '/xamng/case/getSlaves',
    };

    initAddCaseStepModal();

    $('#slave').dropdown({
         fields:{
             name:'host',
             value:'id',
             text:'host'
         },
         apiSettings:{
            action:'get slaves'
         },
         saveRemoteData:false
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

    $('.addStep').click(function(e){
        $('.ui.modal').modal('show');
    });

    $('#steplist').sortable();

});


function getStepsFromPage(){

}

function initAddCaseStepModal(){
    $('.ui.modal').modal({closable:false});
    $('#addStepForm').form({
        fields: {
          elementSign     : 'empty',
          operate   : 'empty',
          elementValue : 'empty'
        }
    });
    $('.ui.primary').api({
        url:'/xamng/case/save',
        method:'post',
        beforeSend:function(settings){
            $('#addStepForm').form('validate form');
            if(!$('#addStepForm').form('is valid')){
                return false
            }
            settings.temp = {}
            settings.temp.elementSign = $('#addStepForm').form('get value','elementSign');
            settings.temp.operate = $('#addStepForm').form('get value','operate');
            settings.temp.elementValue = $('#addStepForm').form('get value','elementValue');
            settings.temp.stepTitle = $('#addStepForm').form('get value','stepTitle');
            settings.data = JSON.stringify({
                caseId:'123123',
                step:{
                    'element':settings.temp.elementSign,
                    'action':settings.temp.operate,
                    'value':settings.temp.elementValue,
                    'title':settings.temp.stepTitle
                }
            });
            return settings;
        },
        onSuccess:function(response, element, xhr){
            $('#steplist').append(
                '<li class="item">'+
                   '<div class="header">'+$('#addStepForm').form('get value','stepTitle')+'</div>'+
                   '<div class="content">'+
                       '<div name="elementUnique" class="ui blue large label">'+$('#addStepForm').form('get value','elementSign')+'</div>'+
                       '<div name="operateType" class="ui blue large label">'+$('#addStepForm').form('get value','operate')+'</div>'+
                       '<div name="elementValue" class="ui blue large label">'+$('#addStepForm').form('get value','elementValue')+'</div>'+
                       '<div class="right floated"><div class="ui small button">删除</div></div>'+
                   '</div>'+
                '</li>'
            );
            $('.ui.form')[0].reset();
            $('.ui.modal').modal('hide');
        }
    });
}