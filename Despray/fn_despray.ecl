IMPORT STD;
fn_despray := function
subfilename := NOTHOR(STD.File.GetSuperFileSubName('~hthor::sh::internship_testing', 1, false));
deSpray_file := STD.File.DeSpray('~' + subfilename,
      'localhost',
        '/var/lib/HPCCSystems/dropzone/' +subfilename,
      -1,
      ,
      ,true
      );


proc_despray := if(subfilename <> '',sequential(deSpray_file,
STD.File.clearsuperfile('~hthor::sh::internship_testing',true)), output('No file despray'));

return proc_despray;

end;
fn_despray:WHEN(CRON('0 0-23/3 * * *'));