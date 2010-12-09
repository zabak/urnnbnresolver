<!doctype html>
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title>URN:NBN</title>
        <%@ page import="cz.incad.urnnbn.search.server.StatisticService" %>
        <%!
            
            public static final long serialVersionUID = 1L;
            StatisticService statisticService = new StatisticService();
            
        %>
		<script language="javascript" src="search/search.nocache.js"></script>
        <script type="text/javascript">
            var info = { "ieCount" : "<%= statisticService.getIECount() %>", "drCount" : "<%= statisticService.getDRCount() %>" };
        </script>
	</head>
	<body>
	</body>
</html>
