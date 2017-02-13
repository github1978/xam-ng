$(document).ready(function(){

    $.fn.api.settings.api = {
      'get slaves' : '/xamng/case/getSlaves',
    };

    initAddCaseStepModal();

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
    $('.ui.primary').click(function(e){
        var elementSign = $('#addStepForm').form('get value','elementSign');
        var operate = $('#addStepForm').form('get value','operate');
        var elementValue = $('#addStepForm').form('get value','elementValue');
        var stepTitle = $('#addStepForm').form('get value','stepTitle');
        $('#addStepForm').form('validate form');
        if($('#addStepForm').form('is valid')){
            $.ajax({
                type: "POST",
                url:'/xamng/case/save',
                contentType:"application/json; charset=utf-8",
                data:JSON.stringify({
                    caseId:'123123',
                    step:{
                        'element':elementSign,
                        'action':operate,
                        'value':elementValue,
                        'title':stepTitle
                    }
                }),
                dataType: "json",
                success:function(response){
                    $('#steplist').append(
                        '<li class="item">'+
                           '<div class="header">'+stepTitle+'</div>'+
                           '<div class="content">'+
                               '<div name="elementUnique" class="ui blue large label">'+elementSign+'</div>'+
                               '<div name="operateType" class="ui blue large label">'+operate+'</div>'+
                               '<div name="elementValue" class="ui blue large label">'+elementValue+'</div>'+
                               '<div class="right floated"><div class="ui small button">删除</div></div>'+
                           '</div>'+
                        '</li>'
                    );
                    $('.ui.form')[0].reset();
                    $('.ui.modal').modal('hide');
                }
            });
        }
    });
}