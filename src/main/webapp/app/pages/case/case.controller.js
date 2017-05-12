(function(){

    angular
        .module('BlurAdmin.pages.case.ctrl',[])
        .controller('CaseController',['$scope','$uibModal','caseServices','initData',CaseController])
        .controller('addCaseController',['$scope','$http','baConfig',addCaseController]);

    function CaseController($scope,$uibModal,caseServices,initData){

        $scope.caseData = initData.list;
        $scope.queryCases = function(){
            caseServices.getCaseList().then(function(res){
                $scope.caseData = res.list;
            });
        };
        $scope.open = function (page, size) {
          var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: 'app/pages/case/addCase.html',
            controller:'addCaseController',
            resolve: {
              items: function () {
                return $scope.items;
              }
            }
          });
          console.log(modalInstance);
        };
    }

    function addCaseController($scope,$http,baConfig){

        $scope.getSlaves = getSlaves;
        $scope.excuteCase = excuteCase;
        $scope.saveCase = saveCase;
        $scope.addStep = addStep;
        $scope.selectedBrowser = {};
        $scope.selectedSlave = {};
        $scope.selectedOperation = {};
        $scope.operates = [
            {value:"click"},
            {value:"select"},
            {value:"type"},
            {value:"go"}
        ];

        function saveCase(){
            $http.post(
                "/xamng/case/save",
                {
                    caseId:'121212',
                    caseDetail:'hehe',
                    steps:[
                        {detail:'hehe',element:'hehe',operate:'hehe',target:'hehe'},
                        {detail:'hehe1',element:'hehe1',operate:'hehe1',target:'hehe1'}
                    ]
                }
            ).success(
                function(data, status, headers, config) {
                    console.log(data)
                }
            );
        }
        function getSlaves(){
            $http.get("/xamng/case/getSlaves").then(
                function (response) {
                    $scope.slaves = response.data.results;
                    return $scope.slaves;
                }
            );
        }
        function excuteCase(){
            $http.post(
                "/xamng/case/excute",
                {
                    slave:$scope.selectedSlave.value.id,
                    browser:$scope.selectedBrowser.value
                },
                {
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                    transformRequest: function(data){return $.param(data);}
                }
            ).success(function(data, status, headers, config){

            });
        }
        function addStep(){
            $scope.steps.push($($('#addStepPanel').find('input')[0]).val());
        };

        stepListController($scope,baConfig);

      }

    function stepListController(stepListScope,vbaconfig){
        stepListScope.transparent = vbaconfig.theme.blur;
        var dashboardColors = vbaconfig.colors.dashboard;
        var colors = [];

        stepListScope.sortableOptions = {
            update: function(e, ui) {
                var resArr = [];
                for (var i = 0; i < stepListScope.todoList.length; i++) {
                    resArr.push(stepListScope.todoList[i].i);
                }
                console.log(stepListScope.todoList)
            }
        };

        for (var key in dashboardColors) {
          colors.push(dashboardColors[key]);
        }

        function getRandomColor() {
          var i = Math.floor(Math.random() * (colors.length - 1));
          return colors[i];
        }

        stepListScope.todoList = [];

        stepListScope.todoList.forEach(function(item) {
          item.color = getRandomColor();
        });

        stepListScope.addToDoItem = function (event, clickPlus) {
          if ((clickPlus || event.which === 13)
          && (stepListScope.vm.newTodoText!='' && stepListScope.vm.newTodoText!=undefined)) {
            stepListScope.todoList.push({
              step: {
                detail:stepListScope.vm.detail,
                element:stepListScope.vm.newTodoText,
                operate:stepListScope.vm.operate.value,
                target:stepListScope.vm.target
              },
              color: getRandomColor(),
            });
            stepListScope.vm.newTodoText = '';
            stepListScope.vm.target = '';
            stepListScope.vm.operate = '';
            stepListScope.vm.detail = '';
          }
        };
      }

})();