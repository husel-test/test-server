#!/bin/sh
PROG_NAME=$0
ACTION=$1
usage() {
    echo "Usage: $PROG_NAME {start|stop}"
    exit 1;
}

if [ $# -lt 1 ]; then
    usage
fi
##########################################################################################
FUNSERVER_HOME=/opt/${web.project.name}
FUNSERVER_LOG_PATH=${log.base.path}
FUNSERVER_STDOUT_PATH="$FUNSERVER_LOG_PATH/stdout.log"
FUNSERVER_GC_LOG="$FUNSERVER_LOG_PATH/gc.log"
JAVA_START_OPT="$JAVA_START_OPT -Dlogback.configurationFile=$FUNSERVER_HOME/conf/logback.xml"
JAVA_START_OPT="$JAVA_START_OPT -Dfunserver.home=$FUNSERVER_HOME"
##########################################################################################
start()
{
	##########################################################################################
	JAVA_START_OPT="-server -d64 -Xms3072m -Xmx3072m -XX:PermSize=32M -XX:MaxPermSize=256M -Xmn1024m -Xss1024k"
	JAVA_START_OPT="$JAVA_START_OPT -XX:+PrintHeapAtGC -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationStoppedTime -XX:+HeapDumpOnOutOfMemoryError -Xloggc:$FUNSERVER_GC_LOG"							
	JAVA_START_OPT="$JAVA_START_OPT -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:ParallelGCThreads=20 -XX:+CMSClassUnloadingEnabled"
	JAVA_START_OPT="$JAVA_START_OPT -XX:+CMSPermGenSweepingEnabled -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=2"
	JAVA_START_OPT="$JAVA_START_OPT -XX:+UseFastAccessorMethods -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=20 -XX:GCTimeRatio=19 -Xnoclassgc"
	JAVA_START_OPT="$JAVA_START_OPT -XX:+DisableExplicitGC -XX:-CMSParallelRemarkEnabled -XX:CMSInitiatingOccupancyFraction=70"
	JAVA_START_OPT="$JAVA_START_OPT -XX:SoftRefLRUPolicyMSPerMB=0"
	##########################################################################################
	echo "正在启动foscam容器..."
	if [ -x "$FUNSERVER_GC_LOG" ]; then  
		mv -f $FUNSERVER_GC_LOG "$FUNSERVER_GC_LOG..`date '+%Y%m%d%H%M%S'`"
	fi   
	if [ -x "$FUNSERVER_STDOUT_PATH" ]; then  
		rm -f $FUNSERVER_STDOUT_PATH
    	touch $FUNSERVER_STDOUT_PATH 
	fi
	nohup java $JAVA_START_OPT -classpath "$JAVA_HOME"/lib/tools.jar:$FUNSERVER_HOME/bin/startup.jar org.foscam.container.startup.Startup start > $FUNSERVER_STDOUT_PATH 2>&1 &
	tail -f $FUNSERVER_STDOUT_PATH
}

stop()
{
	echo "正在停foscam容器..."
	nohup java $JAVA_START_OPT -classpath "$JAVA_HOME"/lib/tools.jar:$FUNSERVER_HOME/bin/startup.jar org.foscam.container.startup.Startup stop > $FUNSERVER_STDOUT_PATH 2>&1 &
	mv -f $FUNSERVER_STDOUT_PATH "$FUNSERVER_STDOUT_PATH.`date '+%Y%m%d%H%M%S'`"
	mv -f $FUNSERVER_GC_LOG "$FUNSERVER_GC_LOG.`date '+%Y%m%d%H%M%S'`"
}

case "$ACTION" in
    start)
        start
    ;;
    stop)
        stop
    ;;
    *)
        usage
    ;;
esac