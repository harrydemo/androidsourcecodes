#include <QtGui/QApplication>
#include <QResource>
#include <QTextCodec>
#include "preview360.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    QTextCodec::setCodecForTr(QTextCodec::codecForLocale());
    QTextCodec::setCodecForCStrings(QTextCodec::codecForLocale());
    qApp->addLibraryPath("plugins");

    Preview360 w;
    w.show();
    return a.exec();
}
